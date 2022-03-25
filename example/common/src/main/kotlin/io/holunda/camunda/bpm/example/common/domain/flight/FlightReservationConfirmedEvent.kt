package io.holunda.camunda.bpm.example.common.domain.flight

data class FlightReservationConfirmedEvent(
  val passengersName: String,
  val bookingReference: String,
  val outgoingFlight: FlightInfo,
  val incomingFlight: FlightInfo
)
