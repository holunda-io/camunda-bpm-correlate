package io.holunda.camunda.bpm.correlate.persist

interface MessageErrorHandlingStrategy {

  fun evaluateError(entity: MessageEntity, errorDescription: String): MessageEntity?
}
