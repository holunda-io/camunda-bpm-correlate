package io.holunda.camunda.bpm.example.common.domain.flight

import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneOffset

class FlightService {

  private val flights = mapOf<Pair<Location, Location>, String>(
    Location("Hamburg", "HAM") to Location("Berlin", "BER") to "LH-001",
    Location("Berlin", "BER") to Location("Hamburg", "HAM") to "LH-002",
  )

  fun bookFlight(bookFlightCommand: BookFlightCommand): FlightReservationConfirmedEvent {

    val outgoing =
      flights.entries.firstOrNull { it.key.first.cityName == bookFlightCommand.sourceCity && it.key.second.cityName == bookFlightCommand.destinationCity }
    val incoming =
      flights.entries.firstOrNull { it.key.first.cityName == bookFlightCommand.destinationCity && it.key.second.cityName == bookFlightCommand.sourceCity }

    requireNotNull(outgoing) { "Could not find flight from ${bookFlightCommand.sourceCity} to ${bookFlightCommand.destinationCity}" }
    requireNotNull(incoming) { "Could not find flight from ${bookFlightCommand.destinationCity} to ${bookFlightCommand.sourceCity}" }

    return FlightReservationConfirmedEvent(
      passengersName = bookFlightCommand.passengersName,
      bookingReference = bookFlightCommand.bookingReference,
      outgoingFlight = FlightInfo(
        fromAirport = outgoing.key.first.airportCode,
        toAirport = outgoing.key.second.airportCode,
        flightNumber = outgoing.value,
        seat = "7C",
        departure = bookFlightCommand.destinationArrivalDate.setHours(7)
      ),
      incomingFlight = FlightInfo(
        fromAirport = incoming.key.first.airportCode,
        toAirport = incoming.key.second.airportCode,
        flightNumber = incoming.value,
        seat = "7A",
        departure = bookFlightCommand.destinationDepartureDate.setHours(19)
      ),
    )
  }

  data class Location(
    val cityName: String,
    val airportCode: String
  )
}

internal fun OffsetDateTime.setHours(hours: Long) =
  this.toLocalDate().atStartOfDay().plusHours(hours).atOffset(ZoneOffset.UTC)

