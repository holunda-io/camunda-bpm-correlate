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
    fun reduce(acc: MessageMetaDataSnippet, other: MessageMetaDataSnippet): MessageMetaDataSnippet =
      acc.let {
        if (other.messageId != null) {
          it.copy(messageId = other.messageId)
        } else {
          it
        }
      }.let {
        if (other.timeToLive != null) {
          it.copy(timeToLive = other.timeToLive)
        } else {
          it
        }
      }.let {
        if (other.expiration != null) {
          it.copy(expiration = other.expiration)
        } else {
          it
        }
      }.let {
        if (other.payloadEncoding != null) {
          it.copy(payloadEncoding = other.payloadEncoding)
        } else {
          it
        }
      }.let {
        if (other.payloadTypeInfo != TypeInfo.UNKNOWN && it.payloadTypeInfo.overwritePossible) {
          it.copy(payloadTypeInfo = other.payloadTypeInfo)
        } else {
          it
        }
      }.let {
        if (other.messageTimestamp != null) {
          it.copy(messageTimestamp = other.messageTimestamp)
        } else {
          it
        }
      }
  }

  /**
   * Checks if the snippets values are all null.
   */
  fun isEmpty(): Boolean = this::class.memberProperties.all { p -> p.getter.call(this) == null }
}
