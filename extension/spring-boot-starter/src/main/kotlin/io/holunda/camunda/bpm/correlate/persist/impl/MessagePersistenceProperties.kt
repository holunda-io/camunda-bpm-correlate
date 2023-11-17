package io.holunda.camunda.bpm.correlate.persist.impl

/**
 * Properties to configure persistence.
 */
data class MessagePersistenceProperties(
  val messageMaxRetries: Int = 100,
  val messageFetchPageSize: Int = 1000,
  val messageBatchSize: Int = -1
) : MessagePersistenceConfig {
  override fun getMaxRetries(): Int = messageMaxRetries
  override fun getPageSize(): Int = messageFetchPageSize
  override fun batchSizeLimit(): Int = messageBatchSize
}

