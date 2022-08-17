package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.ByteMessage
import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage
import io.holunda.camunda.bpm.correlate.ingres.message.ObjectMessage
import java.util.*

fun messageId() = UUID.randomUUID().toString()

fun emptyMessage() = ObjectMessage(mapOf(), "")

fun emptyMessageMetadata() = MessageMetaData(
  messageId = messageId(),
  payloadTypeInfo = TypeInfo.UNKNOWN,
  payloadEncoding = "",
  timeToLive = null,
  expiration = null
)

fun acceptingFilter() = AcceptingMessageFilter()

fun rejectingFilter() = RejectingMessageFilter()

/**
 * Reject all.
 */
class RejectingMessageFilter : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean = false
}

/**
 * Accept all.
 */
class AcceptingMessageFilter : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean = true
}

/**
 * Type.
 */
class PayloadType

/**
 * Another type.
 */
class PayloadType2

/**
 * Lazy type simulation.
 */
interface LazyLoadingMessage<T> {
  /**
   * Imagine this method is performing expensive or impossible (the return type is not on the classpath) de-serialization.
   */
  fun deserializePayload(): T
}