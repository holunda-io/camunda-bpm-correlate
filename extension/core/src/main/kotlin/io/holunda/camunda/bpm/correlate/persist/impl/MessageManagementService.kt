package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import mu.KLogging
import java.time.Clock

/**
 * Message management.
 */
class MessageManagementService(
  private val messageRepository: MessageRepository,
  private val persistenceConfig: MessagePersistenceConfig,
  private val clock: Clock
) {

  companion object: KLogging()

  fun cleanupExpired() {
    messageRepository.deleteAllById(
      messageRepository.findAll(pageSize = persistenceConfig.getPageSize())
        .filter { it.isExpired(clock) }
        .map { it.id }
    )
  }

  fun listAllMessages(): List<MessageEntity> {
    return messageRepository.findAll(pageSize = persistenceConfig.getPageSize())
  }
}
