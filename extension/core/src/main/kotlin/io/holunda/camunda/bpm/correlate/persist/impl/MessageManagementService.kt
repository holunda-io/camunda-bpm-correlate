package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import io.holunda.camunda.bpm.correlate.persist.RetryInfo
import mu.KLogging
import java.time.Clock
import java.time.Instant

/**
 * Message management.
 */
class MessageManagementService(
  private val messageRepository: MessageRepository,
  private val persistenceConfig: MessagePersistenceConfig,
  private val clock: Clock
) {

  companion object : KLogging()

  /**
   * Delete expired messages.
   */
  fun cleanupExpired() {
    messageRepository.deleteAllById(
      messageRepository.findAllLight(page = 0, pageSize = persistenceConfig.getPageSize())
        .filter { it.isExpired(clock) }
        .map { it.id }
    )
  }

  /**
   * Retrieve all messages.
   */
  fun listAllMessages(): List<MessageEntity> {
    return messageRepository.findAll(page = 0, pageSize = persistenceConfig.getPageSize())
  }

  /**
   * Pauses the message processing setting the time of processing to infinity.
   */
  fun pauseMessageProcessing(messageId: String) {
    changeMessageNextRetry(messageId = messageId, nextRetry = RetryInfo.FAR_FUTURE)
  }

  /**
   * Resume the message correlation.
   */
  fun resumeMessageProcessing(messageId: String) {
    changeMessageNextRetry(messageId = messageId, nextRetry = Instant.now())
  }

  /**
   * Deletes a message.
   */
  fun deleteMessage(messageId: String) {
    getMessageById(messageId).also {
      messageRepository.deleteAllById(listOf(messageId))
    }
  }

  /**
   * Changes next retry time of a message.
   */
  fun changeMessageNextRetry(messageId: String, nextRetry: Instant): MessageEntity {
    return getMessageById(messageId)
      .also {
        requireNotNull(it.nextRetry) { "Message with id $messageId has is not due for retry. Ignoring the change." }
        it.nextRetry = nextRetry
        messageRepository.save(it)
      }
  }

  /**
   * Changes retry attempt count of a message.
   */
  fun changeMessageRetryAttempt(messageId: String, retries: Int): MessageEntity {
    return getMessageById(messageId)
      .also {
        it.retries = retries
        messageRepository.save(it)
      }
  }

  fun getMessageById(messageId: String): MessageEntity {
    return requireNotNull(messageRepository.findByIdOrNull(messageId)) { "Could not find message with id $messageId" }
  }

}

