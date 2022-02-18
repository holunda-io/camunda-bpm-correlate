package io.holunda.camunda.bpm.correlate.correlation.metadata

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
      }
  }

  /**
   * Checks if the snippets values are all null.
   */
  fun isEmpty(): Boolean = this::class.memberProperties.all { p -> p.getter.call(this) == null }
}
