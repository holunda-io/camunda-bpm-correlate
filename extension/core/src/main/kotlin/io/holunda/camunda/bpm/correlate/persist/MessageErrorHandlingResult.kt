package io.holunda.camunda.bpm.correlate.persist

/**
 * Result of message error handling strategy.
 */
sealed class MessageErrorHandlingResult {
  /**
   * Update the entity.
   */
  data class Update(val entity: MessageEntity) : MessageErrorHandlingResult()

  /**
   * Delete the entity.
   */
  data class Delete(val entityId: String) : MessageErrorHandlingResult()

  /**
   * Nothing to do.
   */
  object NoOp : MessageErrorHandlingResult()
}
