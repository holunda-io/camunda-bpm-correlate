package io.holunda.camunda.bpm.example.common.domain.hotel

import java.time.OffsetDateTime

data class BookHotelCommand(
  val guestName: String,
  val bookingReference: String,
  val checkin: OffsetDateTime,
  val checkout: OffsetDateTime,
)
