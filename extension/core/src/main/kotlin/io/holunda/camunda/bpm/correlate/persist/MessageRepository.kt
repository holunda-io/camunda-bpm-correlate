package io.holunda.camunda.bpm.correlate.persist

import java.time.Instant


/**
 * Repository to store and retrieve messages.
 */
interface MessageRepository {

  /**
   * Finds all messages.
   * @param page number start element to fetch from.
   * @param pageSize number elements to fetch.
   * @return list of all messages.
   */
  fun findAll(page: Int, pageSize: Int): List<MessageEntity>

  /**
   * Finds all messages without payload.
   * @param page number start element to fetch from.
   * @param pageSize number elements to fetch.
   * @param faultsOnly flag fetching the errors only.
   * @return list of all messages.
   */
  fun findAllLight(page: Int, pageSize: Int, faultsOnly: Boolean): List<MessageEntity>

  /**
   * Finds a message by id.
   * @param id message id.
   * @return message entity or <code>null</code> if not found.
   */
  fun findByIdOrNull(id: String): MessageEntity?

  /**
   * Inserts a new message.
   * @param message message to save.
   */
  fun insert(message: MessageEntity)

  /**
   * Saves modified message.
   * @param message message to save.
   */
  fun save(message: MessageEntity)

  /**
   * Deletes messages for provided ids.
   * @param ids list of ids of messages to delete.
   */
  fun deleteAllById(ids: List<String>)


  /**
   * Count by status and report.
   * @param maxRetries max retries configured in the system.
   * @param now current time.
   * @param farFuture time considered as far future (to detect pauses).
   * @return current count.
   */
  fun countByStatus(maxRetries: Int, now: Instant, farFuture: Instant): CountByStatus

}

