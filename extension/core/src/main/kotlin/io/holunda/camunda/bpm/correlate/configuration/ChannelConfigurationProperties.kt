package io.holunda.camunda.bpm.correlate.configuration

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
data class ChannelConfigurationProperties(
  val timeToLiveAsString: String? = null
)
