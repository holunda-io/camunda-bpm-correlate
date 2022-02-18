package io.holunda.camunda.bpm.correlate.correlation.impl

import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatch
import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatchResult
import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatchResult.Error
import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatchResult.Success
import io.holunda.camunda.bpm.correlate.correlation.BatchCorrelationService
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.event.*
import mu.KLogging
import org.camunda.bpm.engine.RuntimeService

/**
 * Service responsible for correlation with Camunda Platform engine.
 */
class CamundaBpmCorrelationService(
  private val registry: CamundaCorrelationEventFactoryRegistry,
  private val runtimeService: RuntimeService
): BatchCorrelationService {

  companion object : KLogging()

  /**
   * Correlates the batch.
   * @param correlationBatch batch of messages to correlate.
   * @return result of correlation.
   */
  override fun correlateBatch(correlationBatch: CorrelationBatch): CorrelationBatchResult {
    val successfulCorrelations = mutableListOf<MessageMetaData>()
    correlationBatch.correlationMessages.forEach { (messageMetaData, payload) ->
      val factory = registry.getFactory(messageMetaData)
      if (factory != null) {
        val event = factory.create(messageMetaData, payload)
        if (event != null) {
          // TODO: error strategy "NOOP" could be applied here, so we continue if we do not care
          try {
            correlate(event)
            successfulCorrelations += messageMetaData
          } catch (e: Exception) {
            return Error(successfulCorrelations, messageMetaData, e.stackTraceToString())
          }
        } else {
          return Error(successfulCorrelations, messageMetaData, "Could not map message to event.")
        }
      } else {
        return Error(successfulCorrelations, messageMetaData, "No event factory found for message.")
      }
    }
    return Success(successfulCorrelations)
  }

  fun correlate(event: CamundaCorrelationEvent) {
    when (event.correlationType) {
      CorrelationType.MESSAGE -> correlateMessage(event)
      CorrelationType.SIGNAL -> correlateSignal(event)
    }
  }


  private fun correlateMessage(event: CamundaCorrelationEvent) {
    val builder = runtimeService
      .createMessageCorrelation(event.name)
    when (event.correlationScope) {
      CorrelationScope.GLOBAL -> builder.setVariables(event.variables)
      CorrelationScope.LOCAL -> builder.setVariablesLocal(event.variables)
    }
    event.correlationHint.sanityCheck(logger)
    with(event.correlationHint) {
      if (this.processStart) {
        builder.startMessageOnly()
      }
      if (this.businessKey != null) {
        builder.processInstanceBusinessKey(this.businessKey)
      }
      if (this.correlationVariables.isNotEmpty()) {
        builder.processInstanceVariablesEqual(this.correlationVariables)
      }
      when (this.tenantHint) {
        // only if no tenant hint is used, check for process definition and instance
        TenantHint.NONE -> {
          if (this.processDefinitionId != null) {
            builder.processDefinitionId(this.processDefinitionId)
          }
          if (this.processInstanceId != null) {
            builder.processInstanceId(this.processInstanceId)
          }
          Unit
        }
        TenantHint.WITHOUT_TENANT -> builder.withoutTenantId()
        else -> builder.tenantId(this.tenantHint.tenantId)
      }
    }
    builder.correlate()
  }

  private fun correlateSignal(event: CamundaCorrelationEvent) {
    TODO("Not yet implemented")
  }

}
