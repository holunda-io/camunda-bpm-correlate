package io.holunda.camunda.bpm.correlate.correlation

import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConstructorBinding
data class BatchConfigurationProperties(
  val mode: BatchCorrelationMode = BatchCorrelationMode.FAIL_FIRST,
  @NestedConfigurationProperty
  val query: ScheduleConfigurationProperties,
  @NestedConfigurationProperty
  val cleanup: ScheduleConfigurationProperties,
  ) : BatchConfig {
  override fun getBatchMode(): BatchCorrelationMode = mode
  override fun getQueryPollInitialDelay(): String = query.pollInitialDelay
  override fun getQueryPollInterval(): String = query.pollInterval
  override fun getCleanupPollInitialDelay(): String = cleanup.pollInitialDelay
  override fun getCleanupPollInterval(): String = cleanup.pollInterval
}
