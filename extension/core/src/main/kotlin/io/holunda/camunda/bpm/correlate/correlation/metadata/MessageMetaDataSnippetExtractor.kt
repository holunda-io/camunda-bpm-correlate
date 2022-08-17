package io.holunda.camunda.bpm.correlate.correlation.metadata

import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage

/**
 * Extractor for the metadata.
 */
interface MessageMetaDataSnippetExtractor {

  /**
   * Extracts metadata snippet from the message.
   * @return message metadata if at least one metadata attribute could be extracted, null otherwise.
   */
  fun <P> extractMetaData(message: ChannelMessage<P>): MessageMetaDataSnippet?

  /**
   * Checks if the extractor supports this message.
   */
  fun supports(headers: Map<String, Any>): Boolean
}
