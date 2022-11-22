package io.holunda.camunda.bpm.correlate.dto

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.TimeZone

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
  val inserted: ZonedDateTime,
  val timeToLiveDuration: String?,
  val expiration: ZonedDateTime?,
  val retries: Int = 0,
  val nextRetry: ZonedDateTime? = null,
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
  inserted = inserted.atZone(ZoneOffset.UTC),
  timeToLiveDuration = timeToLiveDuration,
  expiration = expiration?.atZone(ZoneOffset.UTC),
  retries = retries,
  nextRetry = nextRetry?.atZone(ZoneOffset.UTC),
  error = error
)