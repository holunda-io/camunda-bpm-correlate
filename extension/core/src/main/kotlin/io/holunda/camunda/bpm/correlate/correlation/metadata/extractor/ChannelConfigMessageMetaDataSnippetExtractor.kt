package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder

/**
 * Extracts metadata from channel config.
 */
class ChannelConfigMessageMetaDataSnippetExtractor(
  private val channelConfig: ChannelConfig,
  private val payloadDecoders: List<PayloadDecoder>
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

  override fun supports(headers: Map<String, Any>): Boolean {
    return true
  }
}
