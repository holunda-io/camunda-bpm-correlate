package io.holunda.camunda.bpm.correlate.metadata

import io.holunda.camunda.bpm.correlate.message.AbstractGenericMessage

/**
 * Extractor for the metadata.
 */
interface MessageMetaDataSnippetExtractor {

  /**
   * Extracts metadata snippet from the message.
   * @return message metadata if at least one metadata attribute could be extracted, null otherwise.
   */
  fun <P> extractMetaData(message: AbstractGenericMessage<P>): MessageMetaDataSnippet?

  /**
   * Checks if the extractor supports this message.
   */
  fun supports(headers: Map<String, Any>): Boolean
}
