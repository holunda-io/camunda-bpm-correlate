package io.holunda.camunda.bpm.correlate.job

import io.holunda.camunda.bpm.correlate.event.CorrelationHint
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaData

interface CorrelationStrategy {

  /**
   * Determines correlation hints based on message metadata.
   */
  fun correlationSelector(): (MessageMetaData) -> CorrelationHint
}
