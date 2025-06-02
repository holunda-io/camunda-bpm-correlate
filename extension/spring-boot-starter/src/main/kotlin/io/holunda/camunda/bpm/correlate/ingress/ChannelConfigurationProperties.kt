package io.holunda.camunda.bpm.correlate.ingress

/**
 * Configuration properties for a named channel.
 */
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
   * Prefix for the name of beans to create.
   * If not provided the <channel-name>Consumer will be used as bean name of the consumer.
   * If not provided the <channel-name>Converter will be used as bean name of the channel message header converter.
   * If you register beans with those names in your code, the creation is skipped.
   */
  val beanNamePrefix: String? = null,
  /**
   * Additional properties.
   */
  val properties: Map<String, Any> = mapOf()
)
