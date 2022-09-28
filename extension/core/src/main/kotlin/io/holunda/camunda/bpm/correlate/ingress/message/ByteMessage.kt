package io.holunda.camunda.bpm.correlate.ingress.message

/**
 * Message using binary payload.
 */
class ByteMessage(
  override val headers: Map<String, Any>,
  override val payload: ByteArray
) : ChannelMessage<ByteArray>
