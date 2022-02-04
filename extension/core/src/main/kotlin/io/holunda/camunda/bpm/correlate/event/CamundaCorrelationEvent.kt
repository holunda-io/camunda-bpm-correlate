package io.holunda.camunda.bpm.correlate.event

import org.camunda.bpm.engine.variable.VariableMap
import org.camunda.bpm.engine.variable.Variables.createVariables

/**
 * Encapsulates data required for correlation with the process.
 */
data class CamundaCorrelationEvent(
  /**
   * Name of the message or signal, as defined in BPMN process model.
   */
  val name: String,

  /**
   * Map of variables to use by the correlation.
   */
  val variables: VariableMap = createVariables(),

  /**
   * Correlation scope (global or local).
   */
  val correlationScope: CorrelationScope = CorrelationScope.GLOBAL,

  /**
   * Correlation type.
   */
  val correlationType: CorrelationType = CorrelationType.MESSAGE,

  /**
   * Correlation hint required to find the target for the correlation.
   */
  val correlationHint: CorrelationHint
)
