package io.holunda.camunda.bpm.correlate.metadata.extractor

import io.holunda.camunda.bpm.correlate.message.AbstractGenericMessage
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.data.CamundaBpmData.stringVariable
import org.camunda.bpm.engine.variable.Variables.createVariables

/**
 * Implementation of a metadata extractor from message headers.
 */
class HeaderMessageMetaDataSnippetExtractor : MessageMetaDataSnippetExtractor {

  companion object {
    val HEADER_MESSAGE_PAYLOAD_CLASS_NAME = stringVariable("X-Correlate-Payload-FQCN")
    val HEADER_MESSAGE_TTL = stringVariable("X-Correlate-TTL")
    val HEADER_MESSAGE_ID = stringVariable("X-Message-ID")
  }

  private fun readMetaDataSnippet(headers: Map<String, Any>): MessageMetaDataSnippet {
    val reader = reader(createVariables().apply { this.putAll(headers) })
    return MessageMetaDataSnippet(
      payloadClass = reader.getOrNull(HEADER_MESSAGE_PAYLOAD_CLASS_NAME),
      timeToLive = reader.getOrNull(HEADER_MESSAGE_TTL),
      messageId = reader.getOrNull(HEADER_MESSAGE_ID)
    )
  }

  override fun <P> extractMetaData(message: AbstractGenericMessage<P>): MessageMetaDataSnippet? {
    val snippet = readMetaDataSnippet(message.headers)
    return if (snippet.isEmpty()) {
      null
    } else {
      snippet
    }
  }

  override fun supports(headers: Map<String, Any>): Boolean {
    val snippet = readMetaDataSnippet(headers)
    return snippet.messageId != null && snippet.payloadClass != null
  }
}
