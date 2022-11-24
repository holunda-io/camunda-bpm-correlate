package io.holunda.camunda.bpm.example.common.domain

import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * Sets hours.
 */
internal fun OffsetDateTime.setHours(hours: Long) =
  this.toLocalDate().atStartOfDay().plusHours(hours).atOffset(ZoneOffset.UTC)
