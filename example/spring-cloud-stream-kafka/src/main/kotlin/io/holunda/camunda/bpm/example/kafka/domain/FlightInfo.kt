package io.holunda.camunda.bpm.example.kafka.domain

data class FlightInfo(
  val fromAirport: String,
  val toAirport: String,
  val flightNumber: String,
  val seat: String
)
