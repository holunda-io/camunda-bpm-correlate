package io.holunda.camunda.bpm.correlate.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "camunda.correlate")
data class CorrelateConfigurationProperties(
  val foo: String
)
