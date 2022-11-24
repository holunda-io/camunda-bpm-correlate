package io.holunda.camunda.bpm.example.common.domain.hotel

import java.time.OffsetDateTime

/**
 * Book hotel intent.
 */
data class BookHotelCommand(
  val guestName: String,
  val bookingReference: String,
  val checkin: OffsetDateTime,
  val checkout: OffsetDateTime,
)
