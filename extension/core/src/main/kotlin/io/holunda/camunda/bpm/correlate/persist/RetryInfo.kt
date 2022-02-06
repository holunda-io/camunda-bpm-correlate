package io.holunda.camunda.bpm.correlate.persist

import java.time.Instant

/**
 * Information about retries.
 */
data class RetryInfo(
  val retries: Int,
  val nextRetry: Instant?
)
