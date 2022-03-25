package io.holunda.camunda.bpm.correlate.correlation.impl

import io.holunda.camunda.bpm.correlate.correlation.BatchCorrelationMode
import io.holunda.camunda.bpm.correlate.correlation.BatchCorrelationService
import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatch
import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatchResult
import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatchResult.Error
import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatchResult.Success
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.event.*
import mu.KLogging
import org.camunda.bpm.engine.RuntimeService

/**
 * Service responsible for correlation with Camunda Platform engine.
 */
class CamundaBpmBatchCorrelationService(
  private val registry: CamundaCorrelationEventFactoryRegistry,
  private val runtimeService: RuntimeService,
  private val batchCorrelationMode: BatchCorrelationMode
) : BatchCorrelationService {

  companion object : KLogging()

  /**
   * Correlates the batch.
   * @param correlationBatch batch of messages to correlate.
   * @return result of correlation.
   */
  override fun correlateBatch(correlationBatch: CorrelationBatch): CorrelationBatchResult {
    val successfulCorrelations = mutableListOf<MessageMetaData>()
    val errorCorrelations = mutableMapOf<MessageMetaData, String>()
    correlationBatch
      .correlationMessages
      .forEach { message ->
        val factory = registry.getFactory(message.messageMetaData)
        if (factory != null) {
          val event = factory.create(message)
          if (event != null) {
            try {
              correlate(event)
              successfulCorrelations += message.messageMetaData
            } catch (e: Exception) {
              errorCorrelations[message.messageMetaData] = e.stackTraceToString()
            }
          } else {
            errorCorrelations[message.messageMetaData] = "Could not map message to event."
          }
        } else {
          errorCorrelations[message.messageMetaData] = "No event factory found for message."
        }
        // error occurred and the batch correlation mode is set to abort on first error
        if (batchCorrelationMode == BatchCorrelationMode.FAIL_FIRST && errorCorrelations.isNotEmpty()) {
          return Error(successfulCorrelations, errorCorrelations)
        }
      }
    return if (errorCorrelations.isEmpty()) {
      Success(successfulCorrelations)
    } else {
      Error(successfulCorrelations, errorCorrelations)
    }
  }

  /**
   * Performs correlation of the correlation event with process engine.
   * @param event event to correlation. might be a BPMN signal or BPMN message.
   */
  private fun correlate(event: CamundaCorrelationEvent) {
    event.correlationHint.sanityCheck(logger, event.correlationScope, event.correlationType)
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
    val builder = runtimeService
      .createSignalEvent(event.name)
      .setVariables(event.variables)

    with(event.correlationHint) {
      when (this.tenantHint) {
        // only if no tenant hint is used, check for process definition and instance
        TenantHint.NONE -> {
          if (this.processInstanceId != null) {
            builder.executionId(this.executionId)
          }
          Unit
        }
        TenantHint.WITHOUT_TENANT -> builder.withoutTenantId()
        else -> builder.tenantId(this.tenantHint.tenantId)
      }
    }
    builder.send()
  }

}
