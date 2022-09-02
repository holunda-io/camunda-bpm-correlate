package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import mu.KLogging

/**
 * In-memory implementation.
 */
class InMemMessageRepository() : MessageRepository {

  companion object : KLogging()

  private val store: MutableMap<String, MessageEntity> = mutableMapOf()

  override fun findAll(page: Int, pageSize: Int): List<MessageEntity> {
    return store.values.toList().subList(page * pageSize, pageSize)
  }
  
  override fun findAllLight(page: Int, pageSize: Int): List<MessageEntity> {
    return store.values.toList().subList(page * pageSize, pageSize)
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
}
