package io.holunda.camunda.bpm.correlate.persist

/**
 * Strategy to react on error.
 */
interface SingleMessageErrorHandlingStrategy {

  /**
   * Evaluates the error.
   * @param entity original unchanged message entity, the correlation produced the error on.
   * @param errorDescription error description.
   * @return message error handling strategy result.
   */
  fun evaluateMessageError(entity: MessageEntity, errorDescription: String): MessageErrorHandlingResult
}
