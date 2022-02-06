package io.holunda.camunda.bpm.correlate.event

import mu.KLogger


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
) {

  /**
   * Executes sanity check of the correlation hint.
   * @param logger logger to report results.
   */
  fun sanityCheck(logger: KLogger) {
    if (this.tenantHint != TenantHint.WITHOUT_TENANT) {
      if (this.processDefinitionId != null) {
        logger.warn { "The tenant correlation hint was set, so provided process definition id $processDefinitionId is ignored." }
      }
      if (this.processInstanceId != null) {
        logger.warn { "The tenant correlation hint was set, so provided process instance id $processInstanceId is ignored." }
      }
    }
  }

}
