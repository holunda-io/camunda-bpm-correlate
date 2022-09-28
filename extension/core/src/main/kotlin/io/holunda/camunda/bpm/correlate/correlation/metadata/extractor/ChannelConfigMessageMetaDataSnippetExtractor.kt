package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Extracts metadata from channel config.
 */
class ChannelConfigMessageMetaDataSnippetExtractor(
  private val channelConfig: ChannelConfig
) : MessageMetaDataSnippetExtractor {

  override fun <P> extractMetaData(message: ChannelMessage<P>): MessageMetaDataSnippet? {
    val snippet = MessageMetaDataSnippet(
      timeToLive = channelConfig.getMessageTimeToLiveAsString(),
      payloadEncoding = channelConfig.getMessagePayloadEncoding()
    )
    return if (snippet.isEmpty()) {
      null
    } else {
      snippet
    }
  }

  override fun supports(headers: Map<String, Any>): Boolean {
    // supports everything, since it uses information from the properties.
    return true
  }
}
