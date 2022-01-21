package io.holunda.camunda.bpm.correlate.ingres

class ByteMessage(headers: Map<String, Any>, payload: ByteArray) : AbstractGenericMessage<ByteArray>(headers, payload)
