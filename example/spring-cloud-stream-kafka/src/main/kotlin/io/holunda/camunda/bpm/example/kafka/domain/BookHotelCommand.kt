package io.holunda.camunda.bpm.example.kafka.domain

import java.time.OffsetDateTime

data class BookHotelCommand(
  val guestName: String,
  val checkin: OffsetDateTime,
  val checkout: OffsetDateTime,
)
