package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData

/**
 * Represents message with metadata for correlation and typed payload.
 */
data class CorrelationMessage(
  val messageMetaData: MessageMetaData,
  val payload: Any
)
