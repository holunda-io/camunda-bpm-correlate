package io.holunda.camunda.bpm.correlate.ingres

class JsonMessage(
  headers: Map<String, Any>,
  payload: String
) : AbstractGenericMessage<String>(headers = headers, payload = payload)
