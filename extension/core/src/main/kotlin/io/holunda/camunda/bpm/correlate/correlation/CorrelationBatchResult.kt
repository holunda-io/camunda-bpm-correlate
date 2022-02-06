package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData

/**
 * Result of correlation.
 */
sealed interface CorrelationBatchResult {
  /**
   * Correlation success.
   * @param successfulCorrelations successfully correlated messages.
   */
  class Success(val successfulCorrelations: List<MessageMetaData>) : CorrelationBatchResult

  /**
   * Error during correlation of the batch.
   * @param successfulCorrelations successfully correlated messages.
   * @param errorCorrelation error message.
   * @param errorDescription error description.
   */
  class Error(val successfulCorrelations: List<MessageMetaData>, val errorCorrelation: MessageMetaData, val errorDescription: String) : CorrelationBatchResult
}
