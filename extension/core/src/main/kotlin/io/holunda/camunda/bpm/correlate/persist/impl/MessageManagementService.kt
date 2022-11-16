package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageEntity.Companion.FAR_FUTURE
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
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
      messageRepository.findAllLight(page = 0, pageSize = persistenceConfig.getPageSize(), faultsOnly = true)
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
   * @param messageId message id.
   * @return message.
   */
  fun pauseMessageProcessing(messageId: String): MessageEntity {
    return getMessageById(messageId)
      .also {
        it.nextRetry = FAR_FUTURE
        messageRepository.save(it)
      }
  }

  /**
   * Resume the message correlation.
   * @param messageId message id.
   * @return message.
   */
  fun resumeMessageProcessing(messageId: String): MessageEntity {
    return getMessageById(messageId)
      .also {
        if (it.retries == 0) {
          // there are no retries, clear the next retry
          it.nextRetry = null
        } else {
          // if there are retries, set the next retry to now
          it.nextRetry = Instant.now(clock)
        }
        messageRepository.save(it)
      }
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

  /**
   * Changes retry attempt count and next retry instant of a message.
   */
  fun changeMessageRetries(messageId: String, retries: Int, nextRetry: Instant): MessageEntity {
      return getMessageById(messageId)
          .also {
              requireNotNull(it.nextRetry) { "Message with id $messageId has is not due for retry. Ignoring the change." }
              it.retries = retries
              it.nextRetry = nextRetry
              messageRepository.save(it)
          }
  }

  /**
   * Count messages by status.
   */
  fun countMessagesByStatus(): CountByStatus {
    return messageRepository.countByStatus(persistenceConfig.getMaxRetries(), clock.instant(), MessageEntity.FAR_FUTURE)
  }

  /**
   * Retrieves a message by id.
   * @param messageId message id.
   * @return message.
   */
  internal fun getMessageById(messageId: String): MessageEntity {
    return requireNotNull(messageRepository.findByIdOrNull(messageId)) { "Could not find message with id $messageId" }
  }

}

