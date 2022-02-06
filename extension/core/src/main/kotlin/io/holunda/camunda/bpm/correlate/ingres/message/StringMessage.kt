package io.holunda.camunda.bpm.correlate.ingres.message

/**
 * Message using string for payload serialization.
 */
class StringMessage(
  headers: Map<String, Any>,
  payload: String
) : AbstractChannelMessage<String>(headers = headers, encodedPayload = payload)
