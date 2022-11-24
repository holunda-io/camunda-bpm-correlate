package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippet
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.EXPIRATION
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.ID
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.PAYLOAD_ENCODING
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.PAYLOAD_TYPE_CLASS_NAME
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.PAYLOAD_TYPE_NAME
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.PAYLOAD_TYPE_NAMESPACE
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.PAYLOAD_TYPE_REVISION
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.TIMESTAMP
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames.TTL
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage
import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.data.CamundaBpmData.stringVariable
import org.camunda.bpm.engine.variable.Variables.createVariables
import java.time.Instant
import java.time.format.DateTimeParseException

/**
 * Implementation of a metadata extractor from message headers.
 */
open class HeaderMessageMetaDataSnippetExtractor(
  private val enforceMessageId: Boolean,
  private val enforceTypeInfo: Boolean
) : MessageMetaDataSnippetExtractor {

  /**
   * Names of the message headers.
   */
  object HeaderNames {
    /**
     * Header for the full-qualified class name of the payload type.
     */
    const val PAYLOAD_TYPE_CLASS_NAME = "X-CORRELATE-PayloadType-FQCN"

    /**
     * Header for the namespace of the payload type.
     */
    const val PAYLOAD_TYPE_NAMESPACE = "X-CORRELATE-PayloadType-Namespace"

    /**
     * Header for the name of the payload type.
     */
    const val PAYLOAD_TYPE_NAME = "X-CORRELATE-PayloadType-Name"

    /**
     * Header for the revision of the payload type.
     */
    const val PAYLOAD_TYPE_REVISION = "X-CORRELATE-PayloadType-Revision"

    /**
     * Header for the payload encoding.
     */
    const val PAYLOAD_ENCODING = "X-CORRELATE-Payload-Encoding"

    /**
     * Header for the message time-to-live.
     */
    const val TTL = "X-CORRELATE-TTL"

    /**
     * Header for the message expiration.
     */
    const val EXPIRATION = "X-CORRELATE-Expiration"

    /**
     * Header for the message id.
     */
    const val ID = "X-CORRELATE-ID"

    /**
     * Header for the message timestamp.
     */
    const val TIMESTAMP = "X-CORRELATE-Timestamp"
  }

  companion object {
    val HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME = stringVariable(PAYLOAD_TYPE_CLASS_NAME)
    val HEADER_MESSAGE_PAYLOAD_TYPE_NAMESPACE = stringVariable(PAYLOAD_TYPE_NAMESPACE)
    val HEADER_MESSAGE_PAYLOAD_TYPE_NAME = stringVariable(PAYLOAD_TYPE_NAME)
    val HEADER_MESSAGE_PAYLOAD_TYPE_REVISION = stringVariable(PAYLOAD_TYPE_REVISION)
    val HEADER_MESSAGE_PAYLOAD_ENCODING = stringVariable(PAYLOAD_ENCODING)
    val HEADER_MESSAGE_TTL = stringVariable(TTL)
    val HEADER_MESSAGE_EXPIRATION = stringVariable(EXPIRATION)
    val HEADER_MESSAGE_ID = stringVariable(ID)
    val HEADER_MESSAGE_TIMESTAMP = stringVariable(TIMESTAMP)
  }

  override fun <P> extractMetaData(message: ChannelMessage<P>): MessageMetaDataSnippet? {
    val snippet = readMetaDataSnippet(message.headers)
    return if (snippet.isEmpty()) {
      null
    } else {
      snippet
    }
  }

  override fun supports(headers: Map<String, Any>): Boolean {
    val snippet = readMetaDataSnippet(headers)
    val messageIdCheck = if (enforceMessageId) {
      snippet.messageId != null
    } else {
      true
    }
    val typeCheck = if (enforceTypeInfo) {
      snippet.payloadTypeInfo != TypeInfo.UNKNOWN
    } else {
      true
    }
    return messageIdCheck && typeCheck
  }

  private fun readMetaDataSnippet(headers: Map<String, Any>): MessageMetaDataSnippet {
    val reader = reader(createVariables().apply { this.putAll(headers) })
    return MessageMetaDataSnippet(
      messageId = reader.getOrNull(HEADER_MESSAGE_ID),
      payloadTypeInfo = extractTypeInfo(headers),
      payloadEncoding = reader.getOrNull(HEADER_MESSAGE_PAYLOAD_ENCODING),
      timeToLive = reader.getOrNull(HEADER_MESSAGE_TTL),
      expiration = extractInstant(reader.getOrNull(HEADER_MESSAGE_EXPIRATION)),
      messageTimestamp = extractInstant(reader.getOrNull(HEADER_MESSAGE_TIMESTAMP))
    )
  }

  private fun extractInstant(value: String?): Instant? {
    return if (value != null) {
      try {
        Instant.parse(value)
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
