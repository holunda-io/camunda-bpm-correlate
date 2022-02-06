package io.holunda.camunda.bpm.correlate.persist.error

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageErrorHandlingStrategy
import mu.KLogging
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import kotlin.math.pow

class RetryingErrorHandlingStrategy(
  private val clock: Clock,
  private val retryErrorHandlingProperties: RetryErrorHandlingProperties
) : MessageErrorHandlingStrategy {

  companion object : KLogging()

  override fun evaluateError(entity: MessageEntity, errorDescription: String): MessageEntity? {
    return if (isLive(entity)) {
      // still live, no error
      logger.trace { "Error message is still inside TTL, not reporting any error." }
      null
    } else {
      entity.apply {
        val retries = this.retries + 1 // increment retry
        this.retries = retries
        this.nextRetry = calculateNextRetry(now = clock.instant(), retries = retries)
        this.error = error
      }
    }
  }

  private fun calculateNextRetry(now: Instant, retries: Int): Instant {
    return now.plus(2.0.pow(retries).toLong().coerceAtMost(retryErrorHandlingProperties.retryMaxBackoffMinutes), ChronoUnit.MINUTES)
  }


  private fun isLive(entity: MessageEntity): Boolean {
    return if (entity.timeToLive != null) {
      val titleToLiveDuration = try {
        Duration.parse(entity.timeToLive)
      } catch (e: DateTimeParseException) {
        logger.trace { "Error parsing TTL of message ${entity.id}, ignored." }
        null
      }
      if (titleToLiveDuration != null) {
        entity.inserted.plus(titleToLiveDuration).isAfter(clock.instant())
      } else {
        false
      }
    } else {
      false
    }

  }
}
