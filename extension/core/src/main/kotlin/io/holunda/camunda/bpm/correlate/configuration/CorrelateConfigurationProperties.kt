package io.holunda.camunda.bpm.correlate.configuration

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.persist.error.RetryingErrorHandlingProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * Configuration properties.
 */
@ConstructorBinding
data class CorrelateConfigurationProperties(

  /**
   * Channel configuration.
   */
  @NestedConfigurationProperty
  val channels: Map<String, ChannelConfigurationProperties> = mapOf(),

  @NestedConfigurationProperty
  val retry: RetryingErrorHandlingProperties
)
