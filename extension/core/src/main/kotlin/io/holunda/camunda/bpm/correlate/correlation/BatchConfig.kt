package io.holunda.camunda.bpm.correlate.correlation

/**
 * Configuration for the message batch processing during correlation.
 */
interface BatchConfig {
  fun getBatchMode(): BatchCorrelationMode
  fun getQueryPollInitialDelay(): String
  fun getQueryPollInterval(): String
  fun getCleanupPollInitialDelay(): String
  fun getCleanupPollInterval(): String
  fun getQueuePollLockMostInterval(): String
}
