package io.holunda.camunda.bpm.correlate.persist

import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit

class MessageEntity(
  var id: String,
  var payloadEncoding: String,
  var payloadTypeNamespace: String,
  var payloadTypeName: String,
  var payloadTypeRevision: String?,
  var payload: ByteArray = ByteArray(0),
  var inserted: Instant,
  var timeToLiveDuration: String?,
  var expiration: Instant?,
  var retries: Int = 0,
  var nextRetry: Instant? = null,
  var error: String? = null
) {
  companion object {
    val FAR_FUTURE: Instant = Instant.parse("4999-12-03T10:15:30Z") // taking a value that will match into every DB.
  }

  /**
   * Checks if the message is expired.
   */
  fun isExpired(clock: Clock): Boolean {
    return isExpired(clock.instant())
  }

  /**
   * Checks if the message is expired.
   */
  fun isExpired(now: Instant): Boolean {
    return expiration != null && now >= expiration
  }


  fun status(maxRetries: Int): MessageStatus {
    return if (error == null && retries == 0) {
      // since the error is null and the retries are zero, we are good.
      if (nextRetry == FAR_FUTURE) {
        // paused
        MessageStatus.PAUSED
      } else {
        // waiting for correlation
        MessageStatus.IN_PROGRESS
      }
    } else {
      if (retries == maxRetries) {
        // max retries are reached, no correlation will take place
        MessageStatus.MAX_RETRIES_REACHED
      } else {
        if (nextRetry == FAR_FUTURE) {
          // paused
          MessageStatus.PAUSED
        } else {
          // waiting for correlation
          MessageStatus.RETRYING
        }
      }
    }
  }

}
