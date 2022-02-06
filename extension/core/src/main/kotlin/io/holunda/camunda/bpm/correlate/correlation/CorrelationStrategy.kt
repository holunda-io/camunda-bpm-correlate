package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.event.CorrelationHint

/**
 * Correlation strategy.
 * This interface should be implemented by the user.
 */
interface CorrelationStrategy {

  /**
   * Determines correlation hints based on message metadata.
   */
  fun correlationSelector(): (CorrelationMessage) -> CorrelationHint
}
