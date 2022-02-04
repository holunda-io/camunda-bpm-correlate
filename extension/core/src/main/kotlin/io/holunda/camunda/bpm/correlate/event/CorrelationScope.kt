package io.holunda.camunda.bpm.correlate.event

/**
 * Describes the target scope during correlation.
 */
enum class CorrelationScope {
  /**
   * Correlate and set local variables.
   */
  LOCAL,

  /**
   * Correlate and set global variables.
   */
  GLOBAL
}
