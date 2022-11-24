package io.holunda.camunda.bpm.correlate.correlation

/**
 * Configuration for the message batch processing during correlation.
 */
interface BatchConfig {
  /**
   * Retrieves the batch mode.
   */
  fun getBatchMode(): BatchCorrelationMode

  /**
   * Initial delay of the message query polling.
   */
  fun getQueryPollInitialDelay(): String

  /**
   * Interval between the message query polling.
   */
  fun getQueryPollInterval(): String

  /**
   * Initial delay of the message cleanup polling.
   */
  fun getCleanupPollInitialDelay(): String

  /**
   * Interval between the message cleanup polling.
   */
  fun getCleanupPollInterval(): String

  /**
   * Interval for the lock.
   */
  fun getQueuePollLockMostInterval(): String
}
