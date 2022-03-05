package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import java.time.Clock

class MessageCleanupService(
  private val messageRepository: MessageRepository,
  private val persistenceConfig: MessagePersistenceConfig,
  private val clock: Clock
) {

  fun cleanupExpired() {
    messageRepository.deleteAllById(
      messageRepository.findAll(pageSize = persistenceConfig.getPageSize())
        .filter { it.isExpired(clock) }
        .map { it.id }
    )
  }
}
