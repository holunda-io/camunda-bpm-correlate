package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import mu.KLogging

/**
 *
 */
class DefaultCorrelationService(
  private val persistenceService: MessagePersistenceService,
  private val correlationService: BatchCorrelationService,
  private val correlationMetrics: CorrelationMetrics
) {

  companion object : KLogging()

  fun correlate() {
    persistenceService
      .fetchMessageBatches()
      .filterNot { it.correlationMessages.isEmpty() }
      .forEach { batch ->
        try {
          val result = correlationService.correlateBatch(batch)
          logger.trace { "Processing result for batch ${batch.correlationHint}: $result" }
          when (result) {
            is CorrelationBatchResult.Success -> {
              persistenceService.success(successfulCorrelations = result.successfulCorrelations)
              correlationMetrics.incrementSuccess(result.successfulCorrelations.size)
            }
            is CorrelationBatchResult.Error -> {
              persistenceService.success(successfulCorrelations = result.successfulCorrelations)
              correlationMetrics.incrementSuccess(result.successfulCorrelations.size)
              persistenceService.error(errorMessageMetaData = result.errorCorrelation, errorDescription = result.errorDescription)
              correlationMetrics.incrementError()
            }
          }
        } catch (e: Exception) {
          persistenceService.error(
            errorMessageMetaData = batch.correlationMessages.first().messageMetaData,
            errorDescription = e.message ?: "Error without message of type ${e::class.java.name}"
          )
          correlationMetrics.incrementError()
          logger.trace(e) { "Error processing for batch ${batch.correlationHint}" }
        }
      }
  }
}
