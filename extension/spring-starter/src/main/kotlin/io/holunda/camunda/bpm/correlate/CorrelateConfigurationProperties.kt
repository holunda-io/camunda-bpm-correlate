package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.BatchConfigurationProperties
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfigurationProperties
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
   * Channel configuration.
   */
  @NestedConfigurationProperty
  val channels: Map<String, ChannelConfigurationProperties> = mapOf(),

  @NestedConfigurationProperty
  val retry: RetryingErrorHandlingProperties,

  @NestedConfigurationProperty
  val persistence: MessagePersistenceProperties,

  @NestedConfigurationProperty
  val batch: BatchConfigurationProperties
)
