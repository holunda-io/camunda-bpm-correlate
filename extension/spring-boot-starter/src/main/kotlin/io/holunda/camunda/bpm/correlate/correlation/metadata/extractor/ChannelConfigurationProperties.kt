package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConstructorBinding
data class ChannelConfigurationProperties(
  @NestedConfigurationProperty
  val message: MessageMetaDataChannelConfigurationProperties,
  val channelEnabled: Boolean
): ChannelConfig {

  override fun getMessageTimeToLiveAsString(): String? = message.timeToLiveAsString

  override fun getMessagePayloadEncoding(): String? = message.payloadEncoding

  override fun isEnabled(): Boolean = channelEnabled

}
