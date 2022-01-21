package io.holunda.camunda.bpm.correlate.ingres

/**
 * Accepts messages from channels.
 */
interface MessageAcceptor {

  fun <P> accept(message: AbstractGenericMessage<P>)

  fun supports(headers: Map<String, Any>): Boolean
}
