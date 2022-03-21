package io.holunda.camunda.bpm.example.kafka.domain

import java.time.OffsetDateTime

data class FlightInfo(
  val fromAirport: String,
  val toAirport: String,
  val flightNumber: String,
  val departure: OffsetDateTime,
  val seat: String
)
