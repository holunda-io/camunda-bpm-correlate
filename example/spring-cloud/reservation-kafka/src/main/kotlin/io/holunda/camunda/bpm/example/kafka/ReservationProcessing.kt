package io.holunda.camunda.bpm.example.kafka

import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.longVariable
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.customVariable
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.stringVariable
import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightInfo
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelInfo
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
import java.time.OffsetDateTime

/**
 * Process constants.
 */
object ReservationProcessing {

  const val KEY = "io.holunda.example.travel-agency.reservation-processing"

  /**
   * Commands.
   */
  object Commands {
    const val BOOK_FLIGHT = "bookFlight"
    const val BOOK_HOTEL = "bookHotel"
  }

  /**
   * BOMN elements.
   */
  object Elements {
    const val RESERVATION_RECEIVED = "reservation_received"
    const val FLIGHT_RECEIVED = "flight_reservation_received"
    const val HOTEL_RECEIVED = "hotel_reservation_received"
  }

  /**
   * Process variables.
   */
  object Variables {
    val DELAY = longVariable("delay").nonNull
    val RESERVATION_ID = stringVariable("reservationId").nonNull
    val CUSTOMER_NAME = stringVariable("customerName").nonNull
    val DESTINATION_ARRIVAL_DATE = customVariable<OffsetDateTime>("arrivalDate").nonNull
    val DESTINATION_DEPARTURE_DATE = customVariable<OffsetDateTime>("departureDate").nonNull
    val SOURCE = stringVariable("source").nonNull
    val DESTINATION = stringVariable("destination").nonNull

    val HOTEL_INFO = customVariable<HotelInfo>("hotelInfo").nonNull
    val FLIGHT_INFO_OUTGOING = customVariable<FlightInfo>("flightInfoOutgoing").nonNull
    val FLIGHT_INFO_INCOMING = customVariable<FlightInfo>("flightInfoIncoming").nonNull
  }
}

/**
 * Extract state change from the received message.
 */
fun ReservationReceivedEvent.toProcessVariables() =
  builder()
    .set(ReservationProcessing.Variables.RESERVATION_ID, this.reservationId)
    .set(ReservationProcessing.Variables.CUSTOMER_NAME, this.customerName)
    .set(ReservationProcessing.Variables.SOURCE, this.fromCity)
    .set(ReservationProcessing.Variables.DESTINATION, this.toCity)
    .set(ReservationProcessing.Variables.DESTINATION_ARRIVAL_DATE, this.from)
    .set(ReservationProcessing.Variables.DESTINATION_DEPARTURE_DATE, this.to)
    .set(ReservationProcessing.Variables.DELAY, this.delay ?: 10)
    .build()

/**
 * Extract state change from the received message.
 */
fun FlightReservationConfirmedEvent.toProcessVariables() =
  builder()
    .set(ReservationProcessing.Variables.FLIGHT_INFO_INCOMING, this.incomingFlight)
    .set(ReservationProcessing.Variables.FLIGHT_INFO_OUTGOING, this.outgoingFlight)
    .build()

/**
 * Extract state change from the received message.
 */
fun HotelReservationConfirmedEvent.toProcessVariables() =
  builder()
    .set(
      ReservationProcessing.Variables.HOTEL_INFO,
      HotelInfo(
        customerName = this.guestName,
        checkin = this.checkin,
        checkout = this.checkout,
        roomNumber = this.roomNumber
      )
    )
    .build()
