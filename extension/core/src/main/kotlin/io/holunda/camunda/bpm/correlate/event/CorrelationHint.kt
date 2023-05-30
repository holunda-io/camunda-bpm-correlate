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
   * Execution id.
   */
  val executionId: String? = null,
  /**
   * Tenant information.
   */
  val tenantHint: TenantHint = TenantHint.NONE,
  /**
   * Flag if only process start events should be considered.
   */
  val processStart: Boolean = false,
  /**
   * Any value used for grouping messages into batches. Messages with equal groupingKeys will end up in the same batch.
   * Defaults to correlation variables.
   */
  val groupingKey: Any = correlationVariables
) {

  /**
   * Executes sanity check of the correlation hint.
   * @param logger logger to report results.
   * @param scope scope for variables to set during correlation.
   * @param type correlation type.
   */
  fun sanityCheck(logger: KLogger, scope: CorrelationScope, type: CorrelationType) {
    when (type) {
      CorrelationType.MESSAGE -> {
        if (executionId != null) {
          logger.warn { "The executionId hint is ignored by correlation of messages." }
        }
      }

      CorrelationType.SIGNAL -> {
        if (scope == CorrelationScope.LOCAL) {
          logger.warn { "The correlation of signal with local variables is not supported." }
        }
        if (processDefinitionId != null) {
          logger.warn { "The processDefinitionId hint is ignored by correlation of signals." }
        }
        if (processInstanceId != null) {
          logger.warn { "The processDefinitionId hint is ignored by correlation of signals." }
        }
        if (correlationVariables.isNotEmpty()) {
          logger.warn { "The correlationVariables hint is ignored by correlation of signals." }
        }
      }
    }

    if (this.tenantHint != TenantHint.NONE) {
      if (this.processDefinitionId != null) {
        logger.warn { "The tenant correlation hint was set, so provided process definition id $processDefinitionId is ignored." }
      }
      if (this.processInstanceId != null) {
        logger.warn { "The tenant correlation hint was set, so provided process instance id $processInstanceId is ignored." }
      }
    }
  }

}
