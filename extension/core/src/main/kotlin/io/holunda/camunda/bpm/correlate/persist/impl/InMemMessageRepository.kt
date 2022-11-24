package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import io.holunda.camunda.bpm.correlate.persist.MessageStatus
import mu.KLogging
import java.time.Instant

/**
 * In-memory implementation of a repository.
 */
class InMemMessageRepository : MessageRepository {

  companion object : KLogging()

  private val store: MutableMap<String, MessageEntity> = mutableMapOf()

  override fun findAll(page: Int, pageSize: Int): List<MessageEntity> {
    return store.values.toList().subList(page * pageSize, pageSize)
  }

  override fun findAllLight(page: Int, pageSize: Int, faultsOnly: Boolean): List<MessageEntity> {
    return store.values.filter { if (faultsOnly) { it.error != null } else { true } }.toList().subList(page * pageSize, pageSize)
  }

  override fun findByIdOrNull(id: String): MessageEntity? {
    return store[id]
  }

  override fun save(message: MessageEntity) {
    store[message.id] = message
  }

  override fun insert(message: MessageEntity) {
    store[message.id] = message
  }

  override fun deleteAllById(ids: List<String>) {
    ids.forEach {
      store.remove(it)
    }
  }

  override fun countByStatus(maxRetries: Int, now: Instant, farFuture: Instant): CountByStatus {
    return CountByStatus(
      total = store.count().toLong(),
      error = store.values.count { it.error != null }.toLong(),
      maxRetriesReached = store.values.count { it.status(maxRetries) == MessageStatus.MAX_RETRIES_REACHED }.toLong(),
      retrying = store.values.count { it.status(maxRetries) == MessageStatus.RETRYING }.toLong(),
      inProgress = store.values.count { it.status(maxRetries) == MessageStatus.IN_PROGRESS }.toLong(),
      paused = store.values.count { it.status(maxRetries) == MessageStatus.PAUSED }.toLong(),
    )
  }
}
