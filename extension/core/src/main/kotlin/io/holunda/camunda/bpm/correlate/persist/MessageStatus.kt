package io.holunda.camunda.bpm.correlate.persist

/**
 * Current status of the message.
 */
enum class MessageStatus {
  IN_PROGRESS,
  RETRYING,
  PAUSED,
  MAX_RETRIES_REACHED
}