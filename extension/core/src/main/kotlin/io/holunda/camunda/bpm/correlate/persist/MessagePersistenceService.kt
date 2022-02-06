package io.holunda.camunda.bpm.correlate.persist

import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatch
import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.CorrelationStrategy
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage
import io.holunda.camunda.bpm.correlate.ingres.message.ByteMessage
import io.holunda.camunda.bpm.correlate.ingres.message.StringMessage
import io.holunda.camunda.bpm.correlate.persist.MessageErrorHandlingResult.*
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import mu.KLogging
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import java.time.Clock

/**
 * Persistence for messages.
 */
class MessagePersistenceService(
  private val messageRepository: MessageRepository,
  private val decoder: PayloadDecoder,
  private val clock: Clock,
  private val correlationStrategy: CorrelationStrategy,
  private val errorHandlingStrategy: MessageErrorHandlingStrategy
) {

  companion object : KLogging() {
    // FIXME -> move to properties
    val maxRetries = 100
    val maxCount: Int = 100
  }

  /**
   * Fetches messages.
   * @return message batches.
   */
  fun fetchMessageBatches(): List<CorrelationBatch> {

    // retrieve messages (paged).
    val allMessages: List<MessageEntity> = messageRepository.findAll(Pageable.ofSize(maxCount)).content

    // enrich with retry infos
    val messagesWithRetries = allMessages.associate { entity ->

      val typeInfo = TypeInfo.from(
        namespace = entity.payloadTypeNamespace,
        name = entity.payloadTypeName,
        revision = entity.payloadTypeRevision
      )
      val payload: Any = decoder.decode(payloadTypeInfo = typeInfo, payload = entity.payload)

      CorrelationMessage(
        messageMetaData = MessageMetaData(
          messageId = entity.id,
          payloadTypeInfo = typeInfo,
          timeToLive = entity.timeToLive,
          payloadEncoding = entity.payloadEncoding
        ),
        payload = payload
      ) to RetryInfo(
        retries = entity.retries,
        nextRetry = entity.nextRetry
      )
    }

    // build batches
    val batches: List<CorrelationBatch> = messagesWithRetries
      .keys
      .groupBy(correlationStrategy.correlationSelector())
      .map { CorrelationBatch(correlationHint = it.key, correlationMessages = it.value) }

    // filter batches for processing
    return batches.filter { batch ->
      with(batch.correlationMessages) {
        /*
         * Fast access to retry info for message.
         */
        fun retryInfo(message: CorrelationMessage) = messagesWithRetries.getValue(message)
        val hasNoErrors = this.all { m -> retryInfo(m).retries == 0 } // no errors at all
        val dueForRetry = this.all { m -> retryInfo(m).retries <= maxRetries } // no exceed on retry
          && this.any { m -> retryInfo(m).nextRetry != null && retryInfo(m).nextRetry!! <= clock.instant() } // and at least one due

        // take those without errors or if they are due for retry
        hasNoErrors || dueForRetry
      }
    }
  }


  /**
   * Protocol success of correlation.
   */
  fun success(successfulCorrelations: List<MessageMetaData>) {
    messageRepository.deleteAllById(successfulCorrelations.map { meta -> meta.messageId })
  }

  /**
   * Protocol error of correlation.
   */
  fun error(errorMessageMetaData: MessageMetaData, errorDescription: String) {
    val message = messageRepository.findByIdOrNull(errorMessageMetaData.messageId)
    requireNotNull(message) { "Something went wrong, message (message id: ${errorMessageMetaData.messageId}) causing correlation error is not found." }
    when (val result = errorHandlingStrategy.evaluateError(message, errorDescription)) {
      is Update -> messageRepository.save(result.entity)
      is Delete -> messageRepository.deleteById(result.entityId)
      is NoOp -> Unit
    }
  }

  /**
   * Persists the received message.
   * @param metaData metadata extracted from the message.
   * @param channelMessage message to store.
   */
  fun <P, M : AbstractChannelMessage<P>> persistMessage(metaData: MessageMetaData, channelMessage: M) {
    val payload = when (channelMessage) {
      is ByteMessage -> channelMessage.encodedPayload
      is StringMessage -> channelMessage.encodedPayload.toByteArray()
      else -> {
        throw IllegalArgumentException("Unsupported message type for ${channelMessage.headers}")
      }
    }
    messageRepository.save(
      MessageEntity(
        id = metaData.messageId,
        inserted = clock.instant(),
        payloadEncoding = metaData.payloadEncoding,
        payloadTypeNamespace = metaData.payloadTypeInfo.namespace,
        payloadTypeName = metaData.payloadTypeInfo.name,
        payloadTypeRevision = metaData.payloadTypeInfo.revision,
        timeToLive = metaData.timeToLive,
        payload = payload
      )
    )
  }

}
