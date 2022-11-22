package io.holunda.camunda.bpm.correlate.ingress.axon

import org.axonframework.eventhandling.EventMessage

/**
 * Extractor for headers from Axon message.
 */
interface AxonEventMessageHeaderConverter {

  /**
   * Extracts headers.
   * @param eventMessage message received via Axon event bus.
   * @return map of headers.
   */
  fun extractHeaders(eventMessage: EventMessage<*>): Map<String, Any>
}
