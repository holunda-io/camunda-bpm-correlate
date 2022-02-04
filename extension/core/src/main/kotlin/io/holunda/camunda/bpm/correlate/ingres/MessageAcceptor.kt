package io.holunda.camunda.bpm.correlate.ingres

import io.holunda.camunda.bpm.correlate.message.AbstractGenericMessage

/**
 * Accepts messages from channels.
 */
interface MessageAcceptor {

  /**
   * Generic check if the acceptor support this message.
   * @param headers headers to analyse
   * @return <code>true</code>, if the message can be accepted, <code>false</code> otherwise.
   */
  fun supports(headers: Map<String, Any>): Boolean

  /**
   * Accepts the message.
   * @param message message received via channel.
   */
  fun <P> accept(message: AbstractGenericMessage<P>)

}
