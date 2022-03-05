package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Extracts metadata from channel config.
 */
class ChannelConfigMessageMetaDataSnippetExtractor(
  private val channelConfig: ChannelConfig
) : MessageMetaDataSnippetExtractor {

  override fun <P> extractMetaData(message: AbstractChannelMessage<P>): MessageMetaDataSnippet? {
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

  /**
   * Property extractor is used as enricher or default provider for metadata.
   * it can only provide some values, but never all values.
   */
  override fun supports(headers: Map<String, Any>): Boolean {
    return false
  }
}
