package io.holunda.camunda.bpm.correlate.correlation.metadata

import java.time.Instant

/**
 * Message metadata.
 */
data class MessageMetaData(
  /**
   * Message id.
   */
  val messageId: String,
  /**
   * Timestamp of the message.
   */
  val messageTimestamp: Instant,
  /**
   * Payload type info.
   */
  val payloadTypeInfo: TypeInfo,
  /**
   * Payload encoding.
   */
  val payloadEncoding: String,
  /**
   * TTL as duration as string, measured from receipt / insert instant.
   */
  val timeToLive: String?,
  /**
   * Expiration of the message.
   */
  val expiration: Instant?
) {
  /**
   * Create message metadata from the snippet.
   */
  constructor(snippet: MessageMetaDataSnippet) : this(
    messageId = requireNotNull(snippet.messageId) { "Message id must not be null" },
    messageTimestamp = snippet.messageTimestamp ?: Instant.now(),
    payloadTypeInfo = requireNotNull(snippet.payloadTypeInfo) { "Payload type info must not be null" },
    payloadEncoding = requireNotNull(snippet.payloadEncoding) { "Payload encoding must be set" },
    timeToLive = snippet.timeToLive,
    expiration = snippet.expiration
  )
}
