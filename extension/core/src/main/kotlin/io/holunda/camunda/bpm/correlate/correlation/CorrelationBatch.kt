package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.event.CorrelationHint

/**
 * Correlation batch is a collection of messages with the same correlation hint.
 * @param correlationHint target of the correlation.
 * @param correlationMessages messages that should be correlated.
 */
data class CorrelationBatch(
  val correlationHint: CorrelationHint,
  val correlationMessages: List<CorrelationMessage>
)
