package io.holunda.camunda.bpm.correlate.db

import java.time.Instant

/**
 * Message DTO.
 */
class CorrelateMessageDto(
  var id: String,
  var payloadEncoding: String,
  var payloadTypeNamespace: String,
  var payloadTypeName: String,
  var payloadTypeRevision: String?,
  var payload: ByteArray,
  var inserted: Instant,
  var timeToLiveDuration: String?,
  var expiration: Instant?,
  var retries: Int = 0,
  var nextRetry: Instant? = null,
  var error: String? = null
)
