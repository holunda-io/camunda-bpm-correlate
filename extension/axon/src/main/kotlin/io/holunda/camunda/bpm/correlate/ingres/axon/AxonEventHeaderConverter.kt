package io.holunda.camunda.bpm.correlate.ingres.axon

import org.axonframework.eventhandling.EventMessage

/**
 * Extractor for headers from Axon message.
 */
interface AxonEventHeaderConverter {

  /**
   * Extracts headers.
   * @param eventMessage message received via Axon.
   * @return map of headers.
   */
  fun extractHeaders(eventMessage: EventMessage<*>): Map<String, Any>
}
