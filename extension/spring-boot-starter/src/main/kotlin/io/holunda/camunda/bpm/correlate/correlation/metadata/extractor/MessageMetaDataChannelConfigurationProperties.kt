package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
data class MessageMetaDataChannelConfigurationProperties(
  val timeToLiveAsString: String? = null,
  val payloadEncoding: String? = null
)
