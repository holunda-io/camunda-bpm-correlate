package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage
import io.holunda.camunda.bpm.correlate.util.ComponentLike

/**
 * Chain of extractor with the every further extractor overwriting values of previous.
 */
@ComponentLike
class MessageMetadataExtractorChain(
  private val extractors: List<MessageMetaDataSnippetExtractor>
) : MessageMetaDataSnippetExtractor {

  companion object {
    operator fun invoke(vararg extractor: MessageMetaDataSnippetExtractor): MessageMetadataExtractorChain = MessageMetadataExtractorChain(extractor.asList())
  }

  /**
   * Extracts meta data from the message.
   */
  fun <P> extractChainedMetaData(message: ChannelMessage<P>): MessageMetaData {
    val snippet = extractMetaData(message)
    requireNotNull(snippet) { "Meta data must not be null, extraction failed and delivered no valid metadata snippets" }
    return MessageMetaData(snippet)
  }

  override fun <P> extractMetaData(message: ChannelMessage<P>): MessageMetaDataSnippet? {
    if (!supports(message.headers)) {
      return null
    }
    // extract snippets from the chain
    val extractedMetadataSnippets = extractors.mapNotNull { it.extractMetaData(message) }
    // merge
    return extractedMetadataSnippets.reduceOrNull(MessageMetaDataSnippet::reduce)
  }

  /**
   * The chain supports a message with given headers if all extractors support them.
   */
  override fun supports(headers: Map<String, Any>): Boolean {
    return extractors.all { extractor -> extractor.supports(headers) }
  }
}
