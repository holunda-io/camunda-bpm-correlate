package io.holunda.camunda.bpm.example.common.domain.flight

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.UUID

class FlightServiceTest {

  private val flightService = FlightService()

  @Test
  fun `should book flight`() {
    val command = BookFlightCommand(
      passengersName = "Bud Spencer",
      bookingReference = UUID.randomUUID().toString(),
      sourceCity = "Hamburg",
      destinationCity = "Berlin",
      destinationArrivalDate = OffsetDateTime.now().plusDays(2),
      destinationDepartureDate = OffsetDateTime.now().plusDays(4)
    )
    val result = flightService.bookFlight(command)
    assertThat(result.passengersName == command.passengersName)
    assertThat(result.bookingReference == command.bookingReference)
    assertThat(result.outgoingFlight.departure).isEqualToIgnoringHours(command.destinationArrivalDate)
    assertThat(result.incomingFlight.departure).isEqualToIgnoringHours(command.destinationDepartureDate)
  }
}
