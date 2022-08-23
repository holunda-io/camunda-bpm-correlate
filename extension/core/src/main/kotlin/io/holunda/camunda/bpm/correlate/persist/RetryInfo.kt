package io.holunda.camunda.bpm.correlate.persist

import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Information about retries.
 */
data class RetryInfo(
  val retries: Int,
  val nextRetry: Instant?
) {
  companion object {
    val FAR_FUTURE: Instant = Instant.MAX.minus(72, ChronoUnit.HOURS)
  }
}
