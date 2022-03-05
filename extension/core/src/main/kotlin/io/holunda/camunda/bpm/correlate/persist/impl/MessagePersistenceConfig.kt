package io.holunda.camunda.bpm.correlate.persist.impl

interface MessagePersistenceConfig {
  fun getMaxRetries(): Int
  fun getPageSize(): Int
}

