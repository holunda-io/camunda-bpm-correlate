package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_ID
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_TIMESTAMP
import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import java.time.Instant

/**
 * Default header converter for standard kafka headers.
 * Captures message timestamp and message id.
 */
class KafkaStreamChannelMessageHeaderConverter : StreamChannelMessageHeaderConverter {

  /**
   * Use byte array or string headers of Spring Messaging.
   */
  override fun extractMessageHeaders(message: Message<ByteArray>): Map<String, Any> {
    return builder()
      .set(
        HEADER_MESSAGE_TIMESTAMP,
        if (message.headers.timestamp != null) {
          Instant.ofEpochMilli(message.headers.timestamp!!)
        } else {
          Instant.now()
        }.toString()
      )
      .set(HEADER_MESSAGE_ID, message.headers.id!!.toString())
      .build().apply {
        this.putAll(message.headers.normalized())
      }.toMap()
  }

  /**
   * Normalizing headers to string representation.
   */
  fun MessageHeaders.normalized(): Map<String, Any> {
    return this.map { entry ->
      if (entry.value is ByteArray) {
        entry.key to String(entry.value as ByteArray, Charsets.UTF_8)
      } else {
        entry.key to entry.value
      }
    }.toMap()
  }
}
