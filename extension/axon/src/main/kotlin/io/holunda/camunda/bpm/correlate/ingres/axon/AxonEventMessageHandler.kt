package io.holunda.camunda.bpm.correlate.ingres.axon

import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.ingres.message.ByteMessage
import mu.KLogging
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.EventMessageHandler
import org.axonframework.serialization.SerializedObject
import org.axonframework.serialization.Serializer

class AxonEventMessageHandler(
  private val messageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngresMetrics,
  private val axonEventHeaderExtractor: AxonEventHeaderExtractor,
  private val serializer: Serializer,
) : EventMessageHandler {

  companion object : KLogging()

  override fun handle(eventMessage: EventMessage<*>) {
    metrics.incrementReceived()
    val headers = axonEventHeaderExtractor.extractHeaders(eventMessage)
    if (messageAcceptor.supports(headers)) {
      val serializedPayload: SerializedObject<ByteArray> = eventMessage.serializePayload(serializer, ByteArray::class.java)
      messageAcceptor.accept(ByteMessage(headers = headers, payload = serializedPayload.data))
      logger.debug { "Accepted message $headers" }
      metrics.incrementAccepted()
    } else {
      logger.debug { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored()
    }

  }
}
