package io.holunda.camunda.bpm.example.kafka.domain

import java.time.OffsetDateTime

data class HotelReservationReceivedEvent(
  val guestName: String,
  val arrival: OffsetDateTime,
  val departure: OffsetDateTime,
  val roomNumber: String
)
