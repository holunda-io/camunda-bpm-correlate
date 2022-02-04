package io.holunda.camunda.bpm.correlate.persist

import io.holunda.camunda.bpm.correlate.message.AbstractGenericMessage
import io.holunda.camunda.bpm.correlate.message.ByteMessage
import io.holunda.camunda.bpm.correlate.message.JsonMessage
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaData
import mu.KLogging
import org.springframework.data.domain.Pageable
import java.time.Clock

/**
 * Persistence for messages.
 */
class MessagePersistenceService(
  private val messageRepository: MessageRepository,
  private val clock: Clock
) {

  companion object : KLogging()

  /**
   * Persists the received message.
   * @param message message to store.
   */
  fun <P, M : AbstractGenericMessage<P>> persistMessage(metaData: MessageMetaData, message: M) {
    val (encoding, payload) = when (message) {
      is ByteMessage -> Byte::class.java.name to message.payload
      is JsonMessage -> String::class.java.name to message.payload.toByteArray()
      else -> {
        throw IllegalArgumentException("Unsupported message type for ${message.headers}")
      }
    }

    messageRepository.save(
      MessageEntity(
        id = metaData.messageId,
        inserted = clock.instant(),
        payloadEncoding = encoding,
        payload = payload,
        payloadClass = metaData.payloadClass,
        timeToLive = metaData.timeToLive,
      )
    )
  }

  /**
   * Fetches messages.
   * @return list of messages with metadata.
   */
  fun fetchMessages(maxCount: Int): List<Pair<MessageMetaData, Any>> {
    // FIXME -> no errors,
    // FIXME -> maybe already grouped in batches?
    return messageRepository.findAll(Pageable.ofSize(maxCount)).content.map { entity ->
      MessageMetaData(
        messageId = entity.id,
        payloadClass = entity.payloadClass,
        timeToLive = entity.timeToLive
      ) to when (entity.payloadEncoding) {
        Byte::class.java.name -> entity.payload
        String::class.java.name -> String(entity.payload)
        else -> throw IllegalArgumentException("Unsupported encoding ${entity.payloadEncoding}")
      }
    }
  }
}
