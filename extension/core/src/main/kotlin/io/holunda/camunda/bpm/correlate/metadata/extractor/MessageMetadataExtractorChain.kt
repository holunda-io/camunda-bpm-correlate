package io.holunda.camunda.bpm.correlate.metadata.extractor

import io.holunda.camunda.bpm.correlate.message.AbstractGenericMessage
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaDataSnippetExtractor
import org.springframework.stereotype.Component

/**
 * Chain of extractor with the every further extractor overwriting values of previous.
 */
@Component
class MessageMetadataExtractorChain(
  private val extractors: List<MessageMetaDataSnippetExtractor>
) : MessageMetaDataSnippetExtractor {

  companion object {
    operator fun invoke(vararg extractor: MessageMetaDataSnippetExtractor): MessageMetadataExtractorChain = MessageMetadataExtractorChain(*extractor)
  }

  /**
   * Extracts meta data from the message.
   */
  fun <P> extractChainedMetaData(message: AbstractGenericMessage<P>): MessageMetaData {
    val snippet = extractMetaData(message)
    requireNotNull(snippet) { "Meta data must not be null" }
    return MessageMetaData(snippet)
  }

  override fun <P> extractMetaData(message: AbstractGenericMessage<P>): MessageMetaDataSnippet? {
    if (!supports(message.headers)) {
      return null
    }

    // extract snippets from the chain
    val extractedMetadataSnippets = extractors.mapNotNull { it.extractMetaData(message) }
    // merge
    return when (extractedMetadataSnippets.size) {
      0 -> throw IllegalStateException("No meta data could be extracted, bit one of the extractors reported support.")
      1 -> extractedMetadataSnippets[0]
      else -> extractedMetadataSnippets.reduce(MessageMetaDataSnippet::reduce)
    }
  }

  override fun supports(headers: Map<String, Any>): Boolean {
    return extractors.any { it.supports(headers) }
  }
}
