package io.holunda.camunda.bpm.correlate.ingres.message

/**
 * Message using binary payload.
 */
class ByteMessage(
  headers: Map<String, Any>,
  payload: ByteArray
) : AbstractChannelMessage<ByteArray>(headers, payload)
