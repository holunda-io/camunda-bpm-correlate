package io.holunda.camunda.bpm.correlate.metadata

import kotlin.reflect.full.memberProperties

/**
 * An metadata extractor may produce only parts of metadata, represented by metadata snippet.
 */
data class MessageMetaDataSnippet(
  /**
   * Message id.
   */
  var messageId: String? = null,
  /**
   * Type representing the message payload class.
   */
  var payloadClass: String? = null,
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
        if (other.payloadClass != null) {
          it.copy(payloadClass = other.payloadClass)
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


