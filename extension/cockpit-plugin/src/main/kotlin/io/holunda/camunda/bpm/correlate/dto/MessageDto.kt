package io.holunda.camunda.bpm.correlate.dto

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import java.time.Instant

/**
 * Message DTO.
 */
data class MessageDto(
  val id: String,
  val payloadEncoding: String,
  val payloadTypeNamespace: String,
  val payloadTypeName: String,
  val payloadTypeRevision: String?,
  val inserted: Instant,
  val timeToLiveDuration: String?,
  val expiration: Instant?,
  val retries: Int = 0,
  val nextRetry: Instant? = null,
  val error: String? = null
)

/**
 * Mapper.
 */
fun MessageEntity.toDto() = MessageDto(
  id,
  payloadEncoding,
  payloadTypeNamespace,
  payloadTypeName,
  payloadTypeRevision,
  inserted,
  timeToLiveDuration,
  expiration,
  retries,
  nextRetry,
  error
)
