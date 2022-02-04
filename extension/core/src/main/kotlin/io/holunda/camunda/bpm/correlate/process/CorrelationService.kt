package io.holunda.camunda.bpm.correlate.process

import io.holunda.camunda.bpm.correlate.event.*
import io.holunda.camunda.bpm.correlate.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.process.BatchCorrelationResult.Error
import io.holunda.camunda.bpm.correlate.process.BatchCorrelationResult.Success
import mu.KLogging
import org.camunda.bpm.engine.RuntimeService

class CorrelationService(
  private val registry: CamundaCorrelationEventFactoryRegistry,
  private val runtimeService: RuntimeService
) {

  companion object : KLogging()

  fun correlateBatch(messages: List<Pair<MessageMetaData, Any>>): BatchCorrelationResult {
    val successfulCorrelations = mutableListOf<MessageMetaData>()
    messages.forEach { (messageMetaData, payload) ->
      val factory = registry.getFactory(messageMetaData)
      if (factory != null) {
        val event = factory.create(messageMetaData, payload)
        if (event != null) {
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
        TenantHint.WITHOUT_TENANT -> {
          builder.withoutTenantId()
          checkAndReportProcessCorrelationHintsUsingTenant(event.correlationHint)
        }
        else -> {
          builder.tenantId(this.tenantHint.tenantId)
          checkAndReportProcessCorrelationHintsUsingTenant(event.correlationHint)
        }
      }
    }
    builder.correlate()
  }

  private fun correlateSignal(event: CamundaCorrelationEvent) {
    TODO("Not yet implemented")
  }

  private fun checkAndReportProcessCorrelationHintsUsingTenant(correlationHint: CorrelationHint) {
    if (correlationHint.processDefinitionId != null) {
      logger.warn { "The tenant correlation hint was set, so provided process definition id ${correlationHint.processDefinitionId} is ignored." }
    }
    if (correlationHint.processInstanceId != null) {
      logger.warn { "The tenant correlation hint was set, so provided process instance id ${correlationHint.processInstanceId} is ignored." }
    }
  }

}
