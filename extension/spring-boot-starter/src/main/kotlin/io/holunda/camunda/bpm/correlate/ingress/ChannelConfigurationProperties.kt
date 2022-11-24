package io.holunda.camunda.bpm.correlate.ingress

import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Configuration properties for a named channel.
 */
@ConstructorBinding
data class ChannelConfigurationProperties(
  /**
   * Flag to switch the channel on/off.
   */
  val enabled: Boolean = true,
  /**
   * Channel type, see Ingress Adapters.
   */
  val type: String,
  /**
   * Name of the bean to register the consumer.
   */
  val beanName: String? = null,
  /**
   * Additional properties.
   */
  val properties: Map<String, Any> = mapOf()
)