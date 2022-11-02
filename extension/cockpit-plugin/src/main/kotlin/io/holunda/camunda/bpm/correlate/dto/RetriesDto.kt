package io.holunda.camunda.bpm.correlate.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

class RetriesDto(
  @JsonProperty("retries")
  var retries: Int,
  @JsonProperty("nextRetry")
  var nextRetry: ZonedDateTime
)

