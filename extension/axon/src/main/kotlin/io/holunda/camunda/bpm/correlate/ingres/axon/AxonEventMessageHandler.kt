package io.holunda.camunda.bpm.correlate.ingres.axon

import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.ingres.message.ByteMessage
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import mu.KLogging
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.EventMessageHandler

class AxonEventMessageHandler(
  private val messageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngresMetrics,
  private val axonEventHeaderExtractor: AxonEventHeaderExtractor,
  private val encoder: PayloadDecoder,
) : EventMessageHandler {

  companion object : KLogging()

  override fun handle(eventMessage: EventMessage<*>) {
    metrics.incrementReceived()
    val headers = axonEventHeaderExtractor.extractHeaders(eventMessage)
    if (messageAcceptor.supports(headers)) {
      messageAcceptor.accept(ByteMessage(headers = headers, payload = encoder.encode(eventMessage.payload)))
      logger.debug { "Accepted message $headers" }
      metrics.incrementAccepted()
    } else {
      logger.debug { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored()
    }

  }
}
