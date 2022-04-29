package io.holunda.camunda.bpm.correlate.correlation

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
data class ScheduleConfigurationProperties(
  val pollInitialDelay: String,
  val pollInterval: String
)
