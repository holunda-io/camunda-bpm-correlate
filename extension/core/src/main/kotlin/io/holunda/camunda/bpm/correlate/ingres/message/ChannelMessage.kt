package io.holunda.camunda.bpm.correlate.ingres.message

/**
 * Base class for implementation of messages received via channel.
 */
interface ChannelMessage<P> {
  val headers: Map<String, Any>
  val payload: P
}
