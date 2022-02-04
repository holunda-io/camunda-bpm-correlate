package io.holunda.camunda.bpm.correlate.event


/**
 * Hints for correlation.
 */
data class CorrelationHint(
  /**
   * Correlation variables.
   */
  val correlationVariables: Map<String, Any> = mapOf(),
  /**
   * Business key.
   */
  val businessKey: String? = null,
  /**
   * Process definition id.
   */
  val processDefinitionId: String? = null,
  /**
   * Process instance id.
   */
  val processInstanceId: String? = null,
  /**
   * Tenant information.
   */
  val tenantHint: TenantHint = TenantHint.NONE,
  /**
   * Flag if only process start events should be considered.
   */
  val processStart: Boolean = false
)
