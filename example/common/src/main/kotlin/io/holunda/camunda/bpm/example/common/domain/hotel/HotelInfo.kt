package io.holunda.camunda.bpm.example.common.domain.hotel

import java.time.OffsetDateTime

/**
 * Hotel info.
 */
data class HotelInfo(
  val customerName: String,
  val checkin: OffsetDateTime,
  val checkout: OffsetDateTime,
  val roomNumber: String
)
