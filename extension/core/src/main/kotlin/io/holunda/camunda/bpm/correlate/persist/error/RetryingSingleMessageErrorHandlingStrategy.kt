package io.holunda.camunda.bpm.correlate.persist.error

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageErrorHandlingResult
import io.holunda.camunda.bpm.correlate.persist.SingleMessageErrorHandlingStrategy
import mu.KLogging
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import kotlin.math.pow

class RetryingSingleMessageErrorHandlingStrategy(
  private val clock: Clock,
  private val retryErrorHandlingConfig: RetryingErrorHandlingConfig
) : SingleMessageErrorHandlingStrategy {

  companion object : KLogging()

  override fun evaluateMessageError(entity: MessageEntity, errorDescription: String): MessageErrorHandlingResult {
    return if (isAlive(entity)) {
      // still alive, no error
      logger.trace { "Error message is still inside TTL, not reporting any error." }
      MessageErrorHandlingResult.NoOp
    } else {
      MessageErrorHandlingResult.Retry(
        entity.apply {
          val retries = this.retries + 1 // increment retry
          this.retries = retries
          this.nextRetry = calculateNextRetry(now = clock.instant(), retries = retries)
          this.error = errorDescription
        }
      )
    }
  }

  private fun calculateNextRetry(now: Instant, retries: Int): Instant {
    return now.plus(
      retryErrorHandlingConfig.getBackoffExponentBase()
        .pow(retries).toLong()
        .coerceAtMost(retryErrorHandlingConfig.getMaximumBackOffSeconds()), ChronoUnit.MINUTES
    )
  }


  fun isAlive(entity: MessageEntity): Boolean {
    return if (entity.timeToLiveDuration != null) {
      val titleToLiveDuration = try {
        Duration.parse(entity.timeToLiveDuration)
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
