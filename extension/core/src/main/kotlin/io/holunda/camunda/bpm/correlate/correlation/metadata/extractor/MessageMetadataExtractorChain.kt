package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Chain of extractor with the every further extractor overwriting values of previous.
 */
class MessageMetadataExtractorChain(
  private val extractors: List<MessageMetaDataSnippetExtractor>
) : MessageMetaDataSnippetExtractor {

  companion object {
    operator fun invoke(vararg extractor: MessageMetaDataSnippetExtractor): MessageMetadataExtractorChain = MessageMetadataExtractorChain(*extractor)
  }

  /**
   * Extracts meta data from the message.
   */
  fun <P> extractChainedMetaData(message: AbstractChannelMessage<P>): MessageMetaData {
    val snippet = extractMetaData(message)
    requireNotNull(snippet) { "Meta data must not be null, extraction failed and delivered no valid metadata snippets" }
    return MessageMetaData(snippet)
  }

  override fun <P> extractMetaData(message: AbstractChannelMessage<P>): MessageMetaDataSnippet? {
    if (!supports(message.headers)) {
      return null
    }
    // extract snippets from the chain
    val extractedMetadataSnippets = extractors.mapNotNull { it.extractMetaData(message) }
    // merge
    return extractedMetadataSnippets.reduceOrNull(MessageMetaDataSnippet::reduce)
  }

  override fun supports(headers: Map<String, Any>): Boolean {
    return extractors.any { it.supports(headers) }
  }
}
