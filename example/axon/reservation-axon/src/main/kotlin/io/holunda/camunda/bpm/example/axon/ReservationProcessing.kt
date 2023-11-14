package io.holunda.camunda.bpm.example.axon

import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.longVariable
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.customVariable
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.stringVariable
import io.holunda.camunda.bpm.example.axon.ReservationProcessing.Variables
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
   * Command names.
   */
  object Commands {
    const val BOOK_FLIGHT = "bookFlight"
    const val BOOK_HOTEL = "bookHotel"
  }

  /**
   * BPMN element names.
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
    val DELAY = longVariable("delay")
    val RESERVATION_ID = stringVariable("reservationId")
    val CUSTOMER_NAME = stringVariable("customerName")
    val DESTINATION_ARRIVAL_DATE = customVariable<OffsetDateTime>("arrivalDate")
    val DESTINATION_DEPARTURE_DATE = customVariable<OffsetDateTime>("departureDate")
    val SOURCE = stringVariable("source")
    val DESTINATION = stringVariable("destination")

    val HOTEL_INFO = customVariable<HotelInfo>("hotelInfo")
    val FLIGHT_INFO_OUTGOING = customVariable<FlightInfo>("flightInfoOutgoing")
    val FLIGHT_INFO_INCOMING = customVariable<FlightInfo>("flightInfoIncoming")
  }
}

/**
 * Extract state change from the received message.
 */
fun ReservationReceivedEvent.toProcessVariables() =
  builder()
    .set(Variables.RESERVATION_ID, this.reservationId)
    .set(Variables.CUSTOMER_NAME, this.customerName)
    .set(Variables.SOURCE, this.fromCity)
    .set(Variables.DESTINATION, this.toCity)
    .set(Variables.DESTINATION_ARRIVAL_DATE, this.from)
    .set(Variables.DESTINATION_DEPARTURE_DATE, this.to)
    .set(Variables.DELAY, this.delay ?: 10)
    .build()

/**
 * Extract state change from the received message.
 */
fun FlightReservationConfirmedEvent.toProcessVariables() =
  builder()
    .set(Variables.FLIGHT_INFO_INCOMING, this.incomingFlight)
    .set(Variables.FLIGHT_INFO_OUTGOING, this.outgoingFlight)
    .build()

/**
 * Extract state change from the received message.
 */
fun HotelReservationConfirmedEvent.toProcessVariables() =
  builder()
    .set(
      Variables.HOTEL_INFO,
      HotelInfo(
        customerName = this.guestName,
        checkin = this.checkin,
        checkout = this.checkout,
        roomNumber = this.roomNumber
      )
    )
    .build()
