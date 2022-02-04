package io.holunda.camunda.bpm.correlate.message

/**
 * Message using JSON for payload serialization.
 */
class JsonMessage(
  headers: Map<String, Any>,
  payload: String
) : AbstractGenericMessage<String>(headers = headers, payload = payload)
