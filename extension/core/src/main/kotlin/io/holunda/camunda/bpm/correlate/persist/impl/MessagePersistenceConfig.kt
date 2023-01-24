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

  /**
   * Limits the global size of messages after batch building process. Defaults to unlimited.
   * Special value of interest is 1, because only the first message in batch is taken in current run,
   * and all others are ignored and will be processed during the next correlation attempt.
   */
  fun batchSizeLimit(): Int = -1
}

