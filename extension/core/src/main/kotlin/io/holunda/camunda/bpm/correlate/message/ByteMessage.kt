package io.holunda.camunda.bpm.correlate.message

/**
 * Message using binary payload.
 */
class ByteMessage(
  headers: Map<String, Any>,
  payload: ByteArray
) : AbstractGenericMessage<ByteArray>(headers, payload)
