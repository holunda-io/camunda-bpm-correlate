package io.holunda.camunda.bpm.correlate.ingress

import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Configuration properties for a named channel.
 */
@ConstructorBinding
data class ChannelConfigurationProperties(
  val enabled: Boolean = true,
  val type: String,
  val beanName: String? = null,
  val properties: Map<String, Any> = mapOf()
)