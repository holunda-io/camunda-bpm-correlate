package io.holunda.camunda.bpm.correlate.correlation

/**
 * Correlation service for batches of messages.
 */
interface BatchCorrelationService {

  /**
   * Correlates a batch of messages.
   * @param correlationBatch batch to correlate, containing all messages having the same correlation hint.
   * @return correlation batch result.
   */
  fun correlateBatch(correlationBatch: CorrelationBatch): CorrelationBatchResult
}
