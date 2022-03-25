package io.holunda.camunda.bpm.example.common.domain

import java.time.OffsetDateTime

data class ReservationReceivedEvent(
  val reservationId: String,
  val customerName: String,
  val from: OffsetDateTime,
  val to: OffsetDateTime,
  val fromCity: String,
  val toCity: String,
  val delay: Long? = null
)
