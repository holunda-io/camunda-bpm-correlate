package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.event.CorrelationHint

/**
 * Correlation batch is a collection of messages with the same grouping key.
 * @param groupingKey key for this batch.
 * @param correlationMessages messages that should be correlated.
 */
data class CorrelationBatch(
  val groupingKey: Any,
  val correlationMessages: List<CorrelationMessage>
)
