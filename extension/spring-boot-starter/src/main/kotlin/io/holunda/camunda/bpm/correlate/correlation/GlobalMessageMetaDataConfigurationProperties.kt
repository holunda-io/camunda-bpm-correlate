package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.GlobalConfig
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Global metadata configuration properties.
 */
@ConfigurationProperties(prefix = "correlate.message")
data class GlobalMessageMetaDataConfigurationProperties(
  val timeToLiveAsString: String? = null,
  val payloadEncoding: String? = null
) : GlobalConfig {
  override fun getMessageTimeToLiveAsString(): String? = timeToLiveAsString
  override fun getMessagePayloadEncoding(): String? = payloadEncoding
}
