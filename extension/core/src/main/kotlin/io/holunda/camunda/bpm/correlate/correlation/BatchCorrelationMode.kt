package io.holunda.camunda.bpm.correlate.correlation

/**
 * Mode for correlation of the batch.
 */
enum class BatchCorrelationMode {
  /**
   * Stop the correlation of the batch on the first error.
   */
  FAIL_FIRST,

  /**
   * Correlate all messages of the batch.
   */
  ALL
}
