package io.holunda.camunda.bpm.example.common.domain.hotel

import java.time.OffsetDateTime

data class HotelReservationConfirmedEvent(
  val guestName: String,
  val bookingReference: String,
  val checkin: OffsetDateTime,
  val checkout: OffsetDateTime,
  val roomNumber: String
)
