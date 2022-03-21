package io.holunda.camunda.bpm.correlate.ingres.axon

import org.axonframework.eventhandling.EventMessage

interface AxonEventHeaderExtractor {

  fun extractHeaders(eventMessage: EventMessage<*>): Map<String, Any>
}
