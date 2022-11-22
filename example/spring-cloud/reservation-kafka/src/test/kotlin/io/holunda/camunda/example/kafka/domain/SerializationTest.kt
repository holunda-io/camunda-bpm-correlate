package io.holunda.camunda.example.kafka.domain

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.holunda.camunda.bpm.example.common.domain.flight.FlightInfo
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.*

internal class SerializationTest {

  private val mapper = jacksonObjectMapper()
    .findAndRegisterModules()
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  @Test
  fun `serialize and deserialize reservation`() {

    val event = ReservationReceivedEvent(
      reservationId = UUID.randomUUID().toString(),
      customerName = "Chuck Norris",
      from = OffsetDateTime.now().plusDays(2),
      to = OffsetDateTime.now().plusDays(4),
      fromCity = "Hamburg",
      toCity = "Berlin"
    )

    val type = mapper.typeFactory.constructFromCanonical(
      ReservationReceivedEvent::class.java.name
    )

    val jsonString = mapper.writeValueAsString(event)
    val backFromString = mapper.readValue<ReservationReceivedEvent>(jsonString, type)
    assertThat(event.customerName).isEqualTo(backFromString.customerName)

    val jsonBytes = mapper.writeValueAsBytes(event)
    val backFromBytes = mapper.readValue<ReservationReceivedEvent>(jsonBytes, type)
    assertThat(event.customerName).isEqualTo(backFromBytes.customerName)
  }

  @Test
  fun `serialize and deserialize flight`() {

    val event = FlightReservationConfirmedEvent(
      bookingReference = UUID.randomUUID().toString(),
      passengersName = "Chuck Norris",
      outgoingFlight = FlightInfo(
        fromAirport = "HAM",
        toAirport = "BER",
        flightNumber = "LH-001",
        seat = "5C",
        departure = OffsetDateTime.now().plusDays(2),
      ),
      incomingFlight = FlightInfo(
        fromAirport = "BER",
        toAirport = "HAM",
        flightNumber = "LH-002",
        seat = "7A",
        departure = OffsetDateTime.now().plusDays(4),
      )
    )

    val type = mapper.typeFactory.constructFromCanonical(
      FlightReservationConfirmedEvent::class.java.name
    )

    val jsonString = mapper.writeValueAsString(event)
    val backFromString = mapper.readValue<FlightReservationConfirmedEvent>(jsonString, type)
    assertThat(event.bookingReference).isEqualTo(backFromString.bookingReference)

    val jsonBytes = mapper.writeValueAsBytes(event)
    val backFromBytes = mapper.readValue<FlightReservationConfirmedEvent>(jsonBytes, type)
    assertThat(event.bookingReference).isEqualTo(backFromBytes.bookingReference)
  }

}
