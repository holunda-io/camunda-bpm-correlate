package io.holunda.camunda.bpm.correlate.ingres.message

/**
 * Base class for implementation of messages received via channel.
 */
abstract class AbstractChannelMessage<P>(
  val headers: Map<String, Any>,
  val encodedPayload: P
)
