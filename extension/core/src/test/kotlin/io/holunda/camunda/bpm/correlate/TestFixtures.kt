package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.event.CorrelationHint
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage
import io.holunda.camunda.bpm.correlate.ingress.message.ObjectMessage
import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import java.time.Instant
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

fun runningInstanceHint(processInstanceId: String) = CorrelationHint(
  processDefinitionId = "DEF-ID",
  processInstanceId = processInstanceId,
  groupingKey = processInstanceId
)

fun correlationMessage() = CorrelationMessage(
  messageMetaData = emptyMessageMetadata(),
  payload = UUID.randomUUID().toString()
)

fun messageEntity(id: String = UUID.randomUUID().toString()) = MessageEntity(
  id = id,
  payload = ByteArray(0),
  payloadEncoding = "jackson",
  payloadTypeNamespace = requireNotNull(PayloadType::class.java.packageName),
  payloadTypeName = requireNotNull(PayloadType::class.java.simpleName),
  payloadTypeRevision = null,
  inserted = Instant.now(),
  timeToLiveDuration = null,
  expiration = null,
  retries = 0,
  nextRetry = null,
  error = null
)

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
