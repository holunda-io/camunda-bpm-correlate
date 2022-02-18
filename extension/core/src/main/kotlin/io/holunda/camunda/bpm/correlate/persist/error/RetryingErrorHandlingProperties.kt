package io.holunda.camunda.bpm.correlate.persist.error

data class RetryingErrorHandlingProperties(
  /**
   * Maximum backoff in minutes. Defaults to 180 minutes.
   */
  val retryMaxBackoffMinutes: Long = 180
)
