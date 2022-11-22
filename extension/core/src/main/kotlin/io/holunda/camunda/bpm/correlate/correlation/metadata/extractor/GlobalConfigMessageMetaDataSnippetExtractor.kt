package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Extracts metadata from channel config.
 */
class GlobalConfigMessageMetaDataSnippetExtractor(
  private val globalConfig: GlobalConfig
) : MessageMetaDataSnippetExtractor {

  override fun <P> extractMetaData(message: ChannelMessage<P>): MessageMetaDataSnippet? {
    val snippet = MessageMetaDataSnippet(
      timeToLive = globalConfig.getMessageTimeToLiveAsString(),
      payloadEncoding = globalConfig.getMessagePayloadEncoding()
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
