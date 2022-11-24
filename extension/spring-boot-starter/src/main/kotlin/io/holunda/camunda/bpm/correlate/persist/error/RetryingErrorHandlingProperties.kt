package io.holunda.camunda.bpm.correlate.persist.error

import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Properties for retries.
 */
@ConstructorBinding
data class RetryingErrorHandlingProperties(
  /**
   * Maximum backoff in minutes. Defaults to three hours.
   */
  val retryMaxBackoffMinutes: Long = 180,
  /**
   * Backoff exponent base. Defaults to 2, so the increase is 2
   */
  val retryBackoffBase: Double = 2.0

) : RetryingErrorHandlingConfig {
  override fun getMaximumBackOffSeconds(): Long = retryMaxBackoffMinutes
  override fun getBackoffExponentBase(): Double = retryBackoffBase
}
