package io.holunda.camunda.bpm.correlate.correlation.metadata

import java.time.Instant

/**
 * Message metadata.
 *
 * TODO: issue support expiration (business timeout, for example: I will send you an new event anyway tomorrow)
 */
data class MessageMetaData(
  /**
   * Message id.
   */
  val messageId: String,
  /**
   * Payload type info.
   */
  val payloadTypeInfo: TypeInfo,
  /**
   * Payload encoding.
   */
  val payloadEncoding: String,
  /**
   * TTL as duration string.String
   */
  val timeToLive: String?,
  /**
   * Expiration as string.
   */
  val expiration: Instant?
) {
  /**
   * Create message metadata from the snippet.
   */
  constructor(snippet: MessageMetaDataSnippet) : this(
    messageId = requireNotNull(snippet.messageId) { "Message id must not be null" },
    payloadTypeInfo = requireNotNull(snippet.payloadTypeInfo) { "Payload type info must not be null" },
    payloadEncoding = requireNotNull(snippet.payloadEncoding) { "Payload encoding must be set" },
    timeToLive = snippet.timeToLive,
    expiration = snippet.expiration
  )
}
