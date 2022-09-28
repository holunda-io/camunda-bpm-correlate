package io.holunda.camunda.bpm.correlate.ingress.axon

import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.ingress.message.DelegatingChannelMessage
import io.holunda.camunda.bpm.correlate.ingress.message.ObjectMessage
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import mu.KLogging
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.EventMessageHandler

/**
 * Generic event handler for Axon Framework connected to the event bus receiving all events.
 */
class AxonEventMessageHandler(
  private val messageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngressMetrics,
  private val axonEventHeaderConverter: AxonEventHeaderConverter,
  private val encoder: PayloadDecoder,
  private val channel: String,
) : EventMessageHandler {

  companion object : KLogging()

  override fun handle(eventMessage: EventMessage<*>) {
    metrics.incrementReceived(channel)
    val headers = axonEventHeaderConverter.extractHeaders(eventMessage)
    logger.debug { "Received message $headers" }
    // The message acceptor will only get supported messages.
    // This question is answered  by the [MessageMetadataExtractorChain] - a message is supported if all message metadata snippet extractors support them.
    if (messageAcceptor.supports(headers)) {
      messageAcceptor.accept(
        // Make use of the delegating channel message, which makes sure that the access to the payload (causing the de-serialization) is delayed
        // to the first access of the payload. Especially, this makes it possible to develop [MessageFilters] which are checking the header
        // information only and reject messages without a need to de-serialize them.
        DelegatingChannelMessage(
          delegate = ObjectMessage(headers = headers, payload = eventMessage),
          payloadSupplier = { _eventMessage -> encoder.encode(_eventMessage.payload) }
        )
      )
      logger.debug { "Accepted message $headers" }
      metrics.incrementAccepted(channel)
    } else {
      logger.debug { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored(channel)
    }

  }
}
