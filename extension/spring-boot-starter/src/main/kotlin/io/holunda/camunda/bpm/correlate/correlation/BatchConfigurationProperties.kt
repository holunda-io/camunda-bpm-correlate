package io.holunda.camunda.bpm.correlate.correlation

import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * Batch configuration properties.
 */
@ConstructorBinding
data class BatchConfigurationProperties(
  val mode: BatchCorrelationMode = BatchCorrelationMode.FAIL_FIRST,
  @NestedConfigurationProperty
  val query: ScheduleConfigurationProperties,
  @NestedConfigurationProperty
  val cleanup: ScheduleConfigurationProperties,
  @NestedConfigurationProperty
  val cluster: ClusterSetupProperties = ClusterSetupProperties(),
) : BatchConfig {
  override fun getBatchMode(): BatchCorrelationMode = mode
  override fun getQueryPollInitialDelay(): String = query.pollInitialDelay
  override fun getQueryPollInterval(): String = query.pollInterval
  override fun getCleanupPollInitialDelay(): String = cleanup.pollInitialDelay
  override fun getCleanupPollInterval(): String = cleanup.pollInterval
  override fun getQueuePollLockMostInterval(): String =
    requireNotNull(cluster.queuePollLockMostInterval) { "correlate.batch.cluster.queuePollLockMostInterval must be specified if cluster mode is enabled." }
}
