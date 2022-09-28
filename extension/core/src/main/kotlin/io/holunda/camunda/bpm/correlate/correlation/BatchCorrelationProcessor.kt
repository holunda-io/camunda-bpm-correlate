package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import mu.KLogging

/**
 * Main processor driving the correlation.
 */
class BatchCorrelationProcessor(
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
          logger.debug { "Correlating batch ${batch.correlationHint} containing ${batch.correlationMessages.size} messages." }
          val result: CorrelationBatchResult = correlationService.correlateBatch(batch)
          logger.debug { "Processing result for batch ${batch.correlationHint}: $result" }
          when (result) {
            is CorrelationBatchResult.Success -> {
              persistenceService.success(successfulCorrelations = result.successfulCorrelations)
              correlationMetrics.incrementSuccess(result.successfulCorrelations.size)
            }

            is CorrelationBatchResult.Error -> {
              persistenceService.success(successfulCorrelations = result.successfulCorrelations)
              correlationMetrics.incrementSuccess(result.successfulCorrelations.size)
              persistenceService.error(errorCorrelations = result.errorCorrelations)
              correlationMetrics.incrementError(result.errorCorrelations.size)
            }
          }
        } catch (e: Exception) {
          persistenceService.error(
            mapOf(
              Pair(
                first = batch.correlationMessages.first().messageMetaData,
                second = (e.message ?: "Error without message of type ${e::class.java.name}")
              )
            )
          )
          correlationMetrics.incrementError()
          logger.trace(e) { "Error processing for batch ${batch.correlationHint}" }
        }
      }
  }
}
