package io.holunda.camunda.bpm.correlate.persist.error

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
data class RetryErrorHandlingProperties(
  /**
   * Maximum backoff in minutes. Defaults to 180 minutes.
   */
  val retryMaxBackoffMinutes: Long = 180
)
