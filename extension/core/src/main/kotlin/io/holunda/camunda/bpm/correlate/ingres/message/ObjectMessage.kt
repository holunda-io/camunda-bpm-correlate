package io.holunda.camunda.bpm.correlate.ingres.message

/**
 * Message using not nullable object payload.
 */
class ObjectMessage(
  override val headers: Map<String, Any>,
  override val payload: Any
) : ChannelMessage<Any>
