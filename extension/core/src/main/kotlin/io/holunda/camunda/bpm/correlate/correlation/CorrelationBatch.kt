package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.event.CorrelationHint

data class CorrelationBatch(
  val correlationHint: CorrelationHint,

  // FIXME: ordered
  val correlationMessages: List<CorrelationMessage>
)
