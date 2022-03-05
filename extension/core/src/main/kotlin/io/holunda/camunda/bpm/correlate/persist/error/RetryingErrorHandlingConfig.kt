package io.holunda.camunda.bpm.correlate.persist.error

/**
 * Configuration for error retrying strategy.
 */
interface RetryingErrorHandlingConfig {
  /**
   * Maximum timeout between retries in seconds.
   */
  fun getMaximumBackOffSeconds(): Long

  /**
   * Base of the exponential backoff between retries.
   */
  fun getBackoffExponentBase(): Double
}
