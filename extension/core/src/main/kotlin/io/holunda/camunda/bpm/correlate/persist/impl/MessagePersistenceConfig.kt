package io.holunda.camunda.bpm.correlate.persist.impl

/**
 * Configuration for message persistence configuration.
 */
interface MessagePersistenceConfig {
  /**
   * Retrieves the default number of retries.
   */
  fun getMaxRetries(): Int

  /**
   * Retrieves the page size for paging.
   */
  fun getPageSize(): Int
}

