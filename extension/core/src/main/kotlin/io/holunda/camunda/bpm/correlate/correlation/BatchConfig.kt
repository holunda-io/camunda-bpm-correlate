package io.holunda.camunda.bpm.correlate.correlation

interface BatchConfig {
  fun getBatchMode(): BatchCorrelationMode
  fun getQueryPollInitialDelay(): String
  fun getQueryPollInterval(): String
  fun getCleanupPollInitialDelay(): String
  fun getCleanupPollInterval(): String
}
