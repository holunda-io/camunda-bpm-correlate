package io.holunda.camunda.bpm.correlate.correlation

/**
 * Scheduler configuration.
 */
data class ScheduleConfigurationProperties(
  /**
   * Initial delay before polling starts.
   */
  val pollInitialDelay: String,
  /**
   * Poll interval.
   */
  val pollInterval: String
)
