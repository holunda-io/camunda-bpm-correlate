package io.holunda.camunda.bpm.correlate.metadata

/**
 * Message metadata.
 */
data class MessageMetaData(
  /**
   * Message id.
   */
  val messageId: String,
  /**
   * Type representing the message payload class.
   */
  val payloadClass: String,
  /**
   * TTL as duration string.
   */
  val timeToLive: String?,
) {
  /**
   * Create message metadata from the snippet.
   */
  constructor(snippet: MessageMetaDataSnippet) : this(
    messageId = requireNotNull(snippet.messageId) { "Message id must not be null" },
    payloadClass = requireNotNull(snippet.payloadClass) { "Payload class name must not be null" },
    timeToLive = snippet.timeToLive
  )
}
