package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatch
import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.ingres.message.ByteMessage
import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage
import io.holunda.camunda.bpm.correlate.ingres.message.DelegatingChannelMessage
import io.holunda.camunda.bpm.correlate.persist.*
import io.holunda.camunda.bpm.correlate.persist.MessageErrorHandlingResult.*
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import mu.KLogging
import java.time.Clock

/**
 * Persistence for messages.
 */
class DefaultMessagePersistenceService(
  private val messagePersistenceConfig: MessagePersistenceConfig,
  private val messageRepository: MessageRepository,
  private val payloadDecoders: List<PayloadDecoder>,
  private val clock: Clock,
  private val singleMessageCorrelationStrategy: SingleMessageCorrelationStrategy,
  private val singleMessageErrorHandlingStrategy: SingleMessageErrorHandlingStrategy
) : MessagePersistenceService {

  companion object : KLogging()

  object PayloadNotAvailable

  /**
   * Fetch messages and build batches.
   */
  override fun fetchMessageBatches(): List<CorrelationBatch> {

    // retrieve messages.
    val allMessages: List<MessageEntity> = messageRepository.findAll(0, messagePersistenceConfig.getPageSize())

    logger.debug { "Found ${allMessages.size} messages, building batches." }

    // enrich with retry infos
    val messagesWithRetries = allMessages
      .associate { entity ->

        val typeInfo = TypeInfo.from(
          namespace = entity.payloadTypeNamespace,
          name = entity.payloadTypeName,
          revision = entity.payloadTypeRevision
        )
        val messageMetaData = MessageMetaData(
          messageId = entity.id,
          payloadTypeInfo = typeInfo,
          timeToLive = entity.timeToLiveDuration,
          payloadEncoding = entity.payloadEncoding,
          expiration = entity.expiration
        )

        // create the payload or get an exception
        val payloadResult = decodePayloadOrError(entity, typeInfo)

        if (payloadResult.isSuccess) {
          CorrelationMessage(messageMetaData = messageMetaData, payload = payloadResult.getOrNull()!!) to
            RetryInfo(retries = entity.retries, nextRetry = entity.nextRetry)
        } else {
          // eventually persist the message error
          handleErrorMessage(messageMetaData, payloadResult.exceptionOrNull()!!.stackTraceToString(), entity)
          // reload to get the eventually new retry infos
          val updated = messageRepository.findByIdOrNull(messageMetaData.messageId)
            ?: throw IllegalStateException("Unable to load message ${messageMetaData.messageId}. Failure during message decoder error recovery, the batch can't be built.")
          CorrelationMessage(messageMetaData = messageMetaData, payload = PayloadNotAvailable) to RetryInfo(
            retries = updated.retries, nextRetry = updated.nextRetry // use reloaded retry information
          )
        }
      }

    allMessages.forEach {
      logger.debug { "Message ${it.payloadTypeName}, ${it.retries}, ${it.error}" }
    }

    // build batches
    val batches: List<CorrelationBatch> = messagesWithRetries
      .keys
      .groupBy(singleMessageCorrelationStrategy.correlationSelector())
      .map {
        CorrelationBatch(
          correlationHint = it.key,
          correlationMessages = it.value.sortedWith(singleMessageCorrelationStrategy.correlationMessageSorter())
        )
      }

    logger.debug { "Built ${batches.size} batches." }

    /*
     * Fast access fun to retry info for message.
     */
    fun CorrelationMessage.retryInfo() = messagesWithRetries.getValue(this)

    // filter batches for processing
    return batches.filter { batch ->
      with(batch.correlationMessages) {
        val hasNoErrors = this.all { m -> m.retryInfo().retries == 0 } // no errors at all
        val dueForRetry = this.all { m -> m.retryInfo().retries <= messagePersistenceConfig.getMaxRetries() } // no exceed on retry
          && this.any { m -> m.retryInfo().nextRetry != null && m.retryInfo().nextRetry!! <= clock.instant() } // and at least one due

        // take those without errors or if they are due for retry
        hasNoErrors || dueForRetry
      }
    }
  }

  /**
   * Protocol success of correlation.
   */
  override fun success(successfulCorrelations: List<MessageMetaData>) {
    messageRepository.deleteAllById(successfulCorrelations.map { meta -> meta.messageId })
  }

  /**
   * Protocol errors of correlation.
   */
  override fun error(errorCorrelations: Map<MessageMetaData, String>) {
    // TODO: reason if the error handling strategy should work on a single message or on a list of messages
    errorCorrelations.forEach { (errorMessageMetaData, errorDescription) ->
      val message: MessageEntity? = messageRepository.findByIdOrNull(errorMessageMetaData.messageId)
      handleErrorMessage(errorMessageMetaData, errorDescription, message)
    }
  }

  private fun handleErrorMessage(errorMessageMetaData: MessageMetaData, errorDescription: String, message: MessageEntity?) {
    requireNotNull(message) { "Something went wrong, message (message id: ${errorMessageMetaData.messageId}) causing correlation error is not found." }
    when (val result = singleMessageErrorHandlingStrategy.evaluateMessageError(message, errorDescription)) {
      is Retry -> messageRepository.save(result.entity)
      is Drop -> messageRepository.deleteAllById(listOf(result.entityId))
      is NoOp -> Unit
    }
  }


  /**
   * Persists the received message.
   * @param metaData metadata extracted from the message.
   * @param channelMessage message to store.
   */
  override fun <P, M : ChannelMessage<P>> persistMessage(metaData: MessageMetaData, channelMessage: M) {
    val payload = when (channelMessage) {
      is DelegatingChannelMessage<*, *> -> requireIsByteArray(channelMessage.payload) { "Unsupported payload type inside the message delegate. ByteArray was expected, but it was ${channelMessage.payload::class.qualifiedName}" }
      is ByteMessage -> channelMessage.payload
      else -> {
        throw IllegalArgumentException("Unsupported message type for ${channelMessage.headers}")
      }
    }
    messageRepository.insert(
      MessageEntity(
        id = metaData.messageId,
        inserted = clock.instant(),
        payloadEncoding = metaData.payloadEncoding,
        payloadTypeNamespace = metaData.payloadTypeInfo.namespace,
        payloadTypeName = metaData.payloadTypeInfo.name,
        payloadTypeRevision = metaData.payloadTypeInfo.revision,
        timeToLiveDuration = metaData.timeToLive,
        expiration = metaData.expiration,
        payload = payload
      )
    )
    logger.debug { "Saved message ${metaData.messageId}" }
  }

  /*
   * Decodes the payload using the decoder, on any error, delivers that.
   */
  private fun decodePayloadOrError(entity: MessageEntity, typeInfo: TypeInfo): Result<Any> {
    return runCatching {
      val decoder = payloadDecoders.firstOrNull { it.supports(entity.payloadEncoding) }
        ?: throw IllegalStateException("Could not decode message, no payload decoder found for a message ${entity.id} with encoding ${entity.payloadEncoding}")
      decoder.decode(payloadTypeInfo = typeInfo, payload = entity.payload)
    }
  }
}

inline fun requireIsByteArray(value: Any, lazyMessage: () -> Any): ByteArray =
  if (value is ByteArray) {
    value
  } else {
    throw IllegalArgumentException(lazyMessage().toString())
  }


