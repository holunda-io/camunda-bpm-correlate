package io.holunda.camunda.bpm.correlate.persist

import java.time.Clock
import java.time.Instant

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
  /**
   * Checks if the message is expired.
   */
  fun isExpired(clock: Clock): Boolean {
    return expiration != null && clock.instant() >= expiration
  }
}
