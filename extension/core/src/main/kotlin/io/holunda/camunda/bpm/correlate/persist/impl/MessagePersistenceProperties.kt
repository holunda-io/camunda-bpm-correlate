package io.holunda.camunda.bpm.correlate.persist.impl

data class MessagePersistenceProperties(
  val maxRetries: Int = 100,
  val pageSize: Int = 100
)

