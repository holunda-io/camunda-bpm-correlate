package io.holunda.camunda.bpm.correlate.correlation

import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Scheduler configuration.
 */
@ConstructorBinding
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
