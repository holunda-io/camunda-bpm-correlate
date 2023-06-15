package io.holunda.camunda.bpm.correlate.correlation.metadata

import java.time.Instant
import kotlin.reflect.full.memberProperties

/**
 * A metadata extractor may produce only parts of metadata, represented by metadata snippet.
 */
data class MessageMetaDataSnippet(
  /**
   * Message id.
   */
  var messageId: String? = null,
  /**
   * Type name representing the message payload.
   */
  var payloadTypeInfo: TypeInfo = TypeInfo.UNKNOWN,
  /**
   * Payload encoding.
   */
  var payloadEncoding: String? = null,
  /**
   * TTL as duration string.
   */
  var timeToLive: String? = null,
  /**
   * Expiration.
   */
  var expiration: Instant? = null,
  /**
   * Message timestamp
   */
  var messageTimestamp: Instant? = null
) {

  companion object {
    /**
     * Reducer for snippets.
     */
    fun reduce(acc: MessageMetaDataSnippet, other: MessageMetaDataSnippet): MessageMetaDataSnippet = MessageMetaDataSnippet(
        messageId = other.messageId ?: acc.messageId,
        timeToLive = other.timeToLive ?: acc.timeToLive,
        expiration = other.expiration ?: acc.expiration,
        payloadEncoding = other.payloadEncoding ?: acc.payloadEncoding,
        payloadTypeInfo = if(other.payloadTypeInfo != TypeInfo.UNKNOWN && acc.payloadTypeInfo.overwritePossible) other.payloadTypeInfo else acc.payloadTypeInfo,
        messageTimestamp = other.messageTimestamp ?: acc.messageTimestamp
      )
  }

  /**
   * Checks if the snippets values are all null.
   */
  fun isEmpty(): Boolean = this::class.memberProperties.all { p -> p.getter.call(this) == null }
}
