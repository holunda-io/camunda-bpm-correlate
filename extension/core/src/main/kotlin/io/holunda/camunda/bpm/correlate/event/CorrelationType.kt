package io.holunda.camunda.bpm.correlate.event

/**
 * Correlation type describing how the message is correlated.
 */
enum class CorrelationType {
  /**
   * Correlate as BPMN message.
   */
  MESSAGE,

  /**
   * Correlate as BPMN Signal.
   */
  SIGNAL
}
