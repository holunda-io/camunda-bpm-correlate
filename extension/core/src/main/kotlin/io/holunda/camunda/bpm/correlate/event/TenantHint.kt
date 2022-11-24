package io.holunda.camunda.bpm.correlate.event

/**
 * Tenant hint.
 */
data class TenantHint internal constructor(
  /**
   * Tenant id.
   */
  val tenantId: String? = null,
  /**
   * Flag to correlate without tenant.
   */
  val withoutTenant: Boolean = true
) {
  companion object {
    /**
     * No tenant hint.
     */
    val NONE = TenantHint(tenantId = null, withoutTenant = false)

    /**
     * Specify that the message can only be received on executions or process definitions which belongs to no tenant.
     */
    val WITHOUT_TENANT = TenantHint(tenantId = null, withoutTenant = true)

    /**
     * Specify a tenant to deliver the message to. The message can only be received on executions or process definitions which belongs to the given tenant.
     * @param tenantId id of the tenant.
     * @return tenant hint.
     */
    fun forTenant(tenantId: String) = TenantHint(tenantId = tenantId, withoutTenant = false)
  }
}
