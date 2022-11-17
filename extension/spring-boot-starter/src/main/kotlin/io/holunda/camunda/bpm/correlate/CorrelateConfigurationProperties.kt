package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.BatchConfigurationProperties
import io.holunda.camunda.bpm.correlate.correlation.GlobalMessageMetaDataConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.persist.error.RetryingErrorHandlingProperties
import io.holunda.camunda.bpm.correlate.persist.impl.MessagePersistenceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * Configuration properties.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "correlate")
data class CorrelateConfigurationProperties(

  /**
   * Flag to enable or disable camunda correlate. Defaults to true.
   */
  val enabled: Boolean = true,
  /**
   * Channel configuration.
   * The key in this map is the name of the channel, the value is the configuration for it.
   */
  @NestedConfigurationProperty
  val channels: Map<String, ChannelConfigurationProperties> = mapOf(),

  /**
   * Global message configuration.
   */
  @NestedConfigurationProperty
  val message: GlobalMessageMetaDataConfigurationProperties,

  /**
   * Retry error handling configuration.
   */
  @NestedConfigurationProperty
  val retry: RetryingErrorHandlingProperties,

  /**
   * Message persistence configuration.
   */
  @NestedConfigurationProperty
  val persistence: MessagePersistenceProperties,

  /**
   * Message batch configuration.
   */
  @NestedConfigurationProperty
  val batch: BatchConfigurationProperties
)
