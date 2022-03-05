package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage
import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.data.CamundaBpmData.stringVariable
import org.camunda.bpm.engine.variable.Variables.createVariables
import java.time.Instant
import java.time.format.DateTimeParseException

/**
 * Implementation of a metadata extractor from message headers.
 */
class HeaderMessageMetaDataSnippetExtractor : MessageMetaDataSnippetExtractor {

  companion object {
    val HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME = stringVariable("X-Correlate-PayloadType-FQCN")
    val HEADER_MESSAGE_PAYLOAD_TYPE_NAMESPACE = stringVariable("X-Correlate-PayloadType-Namespace")
    val HEADER_MESSAGE_PAYLOAD_TYPE_NAME = stringVariable("X-Correlate-PayloadType-Name")
    val HEADER_MESSAGE_PAYLOAD_TYPE_REVISION = stringVariable("X-Correlate-PayloadType-Revision")
    val HEADER_MESSAGE_PAYLOAD_ENCODING = stringVariable("X-Correlate-Payload-Encoding")
    val HEADER_MESSAGE_TTL = stringVariable("X-Correlate-TTL")
    val HEADER_MESSAGE_EXPIRATION = stringVariable("X-Correlate-Expiration")
    val HEADER_MESSAGE_ID = stringVariable("X-Message-ID")
  }

  override fun <P> extractMetaData(message: AbstractChannelMessage<P>): MessageMetaDataSnippet? {
    val snippet = readMetaDataSnippet(message.headers)
    return if (snippet.isEmpty()) {
      null
    } else {
      snippet
    }
  }

  override fun supports(headers: Map<String, Any>): Boolean {
    val snippet = readMetaDataSnippet(headers)
    return snippet.messageId != null && snippet.payloadTypeInfo != TypeInfo.UNKNOWN
  }


  private fun readMetaDataSnippet(headers: Map<String, Any>): MessageMetaDataSnippet {
    val reader = reader(createVariables().apply { this.putAll(headers) })
    return MessageMetaDataSnippet(
      messageId = reader.getOrNull(HEADER_MESSAGE_ID),
      payloadTypeInfo = extractTypeInfo(headers),
      payloadEncoding = reader.getOrNull(HEADER_MESSAGE_PAYLOAD_ENCODING),
      timeToLive = reader.getOrNull(HEADER_MESSAGE_TTL),
      expiration = extractExpiration(reader.getOrNull(HEADER_MESSAGE_EXPIRATION))
    )
  }

  private fun extractExpiration(expirationString: String?): Instant? {
    return if (expirationString != null) {
      try {
        Instant.parse(expirationString)
      } catch (e: DateTimeParseException) {
        null
      }
    } else {
      null
    }
  }

  private fun extractTypeInfo(headers: Map<String, Any>): TypeInfo {
    val reader = reader(createVariables().apply { this.putAll(headers) })
    val fullQualifiedClassName = reader.getOrNull(HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME)
    return if (fullQualifiedClassName != null) {
      // got a class name
      TypeInfo.fromFQCN(fullQualifiedClassName)
    } else {
      // try from coordinates
      val namespace = reader.getOrNull(HEADER_MESSAGE_PAYLOAD_TYPE_NAMESPACE)
      val name = reader.getOrNull(HEADER_MESSAGE_PAYLOAD_TYPE_NAME)
      val revision = reader.getOrNull(HEADER_MESSAGE_PAYLOAD_TYPE_REVISION)
      TypeInfo.from(namespace = namespace, name = name, revision = revision)
    }
  }
}
