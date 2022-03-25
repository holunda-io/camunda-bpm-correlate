package io.holunda.camunda.bpm.example.common.domain.flight

import java.time.OffsetDateTime

data class BookFlightCommand(
  val passengersName: String,
  val bookingReference: String,
  val sourceCity: String,
  val destinationCity: String,
  val destinationArrivalDate: OffsetDateTime,
  val destinationDepartureDate: OffsetDateTime,
)
