package io.holunda.camunda.bpm.example.kafka.domain

import java.time.OffsetDateTime

data class BookFlightCommand(
  val passengersName: String,
  val source: String,
  val destinationAirport: String,
  val destinationArrivalDate: OffsetDateTime,
  val destinationDepartureDate: OffsetDateTime,
)
