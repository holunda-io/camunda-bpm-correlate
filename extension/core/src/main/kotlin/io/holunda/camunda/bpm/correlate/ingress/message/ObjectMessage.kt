package io.holunda.camunda.bpm.correlate.ingress.message

/**
 * Message using not nullable object payload.
 * @param P type of the payload.
 * @param payload object
 * @param headers header map.
 */
class ObjectMessage<P: Any>(
  override val headers: Map<String, Any>,
  override val payload: P
) : ChannelMessage<P>
