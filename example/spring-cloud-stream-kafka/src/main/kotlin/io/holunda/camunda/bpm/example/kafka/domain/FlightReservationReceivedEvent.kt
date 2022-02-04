package io.holunda.camunda.bpm.example.kafka.domain

data class FlightReservationReceivedEvent(
  val passengersName: String,
  val outgoingFlight: FlightInfo,
  val incomingFlight: FlightInfo
)
