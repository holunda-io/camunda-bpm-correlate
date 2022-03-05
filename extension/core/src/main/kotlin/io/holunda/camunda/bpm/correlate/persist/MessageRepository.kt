package io.holunda.camunda.bpm.correlate.persist


/**
 * Repository to store and retrieve messages.
 */
interface MessageRepository {

  /**
   * Finds all messages.
   * @param pageSize number elements to fetch.
   * @return list of all messages with no expiration or expiration set to a future date.
   */
  fun findAll(pageSize: Int): List<MessageEntity>

  /**
   * Finds a message by id.
   * @param id message id.
   * @return message entity or <code>null</code> if not found.
   */
  fun findByIdOrNull(id: String): MessageEntity?

  /**
   * Saves message.
   * @param message message to save.
   */
  fun save(message: MessageEntity)

  /**
   * Deletes messages for provided ids.
   * @param ids list of ids of messages to delete.
   */
  fun deleteAllById(ids: List<String>)
}
