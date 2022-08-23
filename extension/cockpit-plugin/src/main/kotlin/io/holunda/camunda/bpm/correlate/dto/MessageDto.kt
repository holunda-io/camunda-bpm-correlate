package io.holunda.camunda.bpm.correlate.dto

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Message DTO.
 */
data class MessageDto(
  val id: String,
  val status: String,
  val payloadEncoding: String,
  val payloadTypeNamespace: String,
  val payloadTypeName: String,
  val payloadTypeRevision: String?,
  val inserted: LocalDateTime,
  val timeToLiveDuration: String?,
  val expiration: LocalDateTime?,
  val retries: Int = 0,
  val nextRetry: LocalDateTime? = null,
  val error: String? = null
)

/**
 * Mapper.
 */
fun MessageEntity.toDto(maxRetries: Int) = MessageDto(
  id = id,
  status = this.status(maxRetries).name,
  payloadEncoding = payloadEncoding,
  payloadTypeNamespace = payloadTypeNamespace,
  payloadTypeName = payloadTypeName,
  payloadTypeRevision = payloadTypeRevision,
  inserted = inserted.atLocalTimeZone(),
  timeToLiveDuration = timeToLiveDuration,
  expiration = expiration?.atLocalTimeZone(),
  retries = retries,
  nextRetry = nextRetry?.atLocalTimeZone(),
  error = error
)

/**
 * Format date to local dates, as Camunda does it in cockpit.
 */
fun Instant.atLocalTimeZone(): LocalDateTime = this.atZone(ZoneId.systemDefault()).toLocalDateTime()