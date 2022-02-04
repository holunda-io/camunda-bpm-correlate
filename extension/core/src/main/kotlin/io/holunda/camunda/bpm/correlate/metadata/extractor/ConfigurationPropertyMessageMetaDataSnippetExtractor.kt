package io.holunda.camunda.bpm.correlate.metadata.extractor

import io.holunda.camunda.bpm.correlate.configuration.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.message.AbstractGenericMessage
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaDataSnippetExtractor

/**
 * Extracts metadata from channel configuration properties.
 */
class ConfigurationPropertyMessageMetaDataSnippetExtractor(
  private val channelConfigurationProperties: ChannelConfigurationProperties
) : MessageMetaDataSnippetExtractor {

  override fun <P> extractMetaData(message: AbstractGenericMessage<P>): MessageMetaDataSnippet? {
    val snippet = MessageMetaDataSnippet(timeToLive = channelConfigurationProperties.timeToLiveAsString)
    return if (snippet.isEmpty()) {
      null
    } else {
      snippet
    }
  }

  /**
   * Property extractor is used as enricher or default provider for metadata.
   */
  override fun supports(headers: Map<String, Any>): Boolean {
    return false
  }
}
