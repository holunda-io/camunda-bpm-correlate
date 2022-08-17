package io.holunda.camunda.bpm.correlate.ingres.axon

import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.ingres.message.ByteMessage
import io.holunda.camunda.bpm.correlate.ingres.message.DelegatingChannelMessage
import io.holunda.camunda.bpm.correlate.ingres.message.ObjectMessage
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import mu.KLogging
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.EventMessageHandler

/**
 * Generic event handler for Axon Framework connected to the event bus receiving all events.
 */
class AxonEventMessageHandler(
  private val messageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngresMetrics,
  private val axonEventHeaderConverter: AxonEventHeaderConverter,
  private val encoder: PayloadDecoder
) : EventMessageHandler {

  companion object : KLogging()

  override fun handle(eventMessage: EventMessage<*>) {
    metrics.incrementReceived()
    val headers = axonEventHeaderConverter.extractHeaders(eventMessage)
    // The message acceptor will only get supported messages.
    // This question is answered  by the [MessageMetadataExtractorChain] - a message is supported if all message metadata snippet extractors support them.
    if (messageAcceptor.supports(headers)) {
      messageAcceptor.accept(
        // Make use of the delegating channel message, which makes sure that the access to the payload (causing the de-serialization) is delayed
        // to the first access of the payload. Especially, this makes it possible to develop [MessageFilters] which are checking the header
        // information only and reject messages without a need to de-serialize them.
        DelegatingChannelMessage(
          delegate = ObjectMessage(headers = headers, payload = eventMessage),
          payloadSupplier = { payload -> encoder.encode((payload as EventMessage<*>).payload) }
        )
      )
      logger.debug { "Accepted message $headers" }
      metrics.incrementAccepted()
    } else {
      logger.debug { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored()
    }

  }
}
