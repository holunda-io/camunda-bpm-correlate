package io.holunda.camunda.bpm.example.kafka.domain

import java.time.OffsetDateTime

data class HotelInfo(
  val customerName: String,
  val checkin: OffsetDateTime,
  val checkout: OffsetDateTime,
  val roomNumber: String
)
