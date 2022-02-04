package io.holunda.camunda.bpm.correlate.process

import io.holunda.camunda.bpm.correlate.metadata.MessageMetaData

/**
 * Result of correlation.
 */
sealed interface BatchCorrelationResult {
  class Success(val successfulCorrelations: List<MessageMetaData>) : BatchCorrelationResult
  class Error(val successfulCorrelations: List<MessageMetaData>, val errorCorrelation: MessageMetaData, val errorDescription: String) : BatchCorrelationResult
}
