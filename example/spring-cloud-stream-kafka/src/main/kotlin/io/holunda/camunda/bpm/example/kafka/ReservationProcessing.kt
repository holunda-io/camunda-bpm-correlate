package io.holunda.camunda.bpm.example.kafka

import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.customVariable
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.stringVariable
import io.holunda.camunda.bpm.example.kafka.domain.FlightInfo
import io.holunda.camunda.bpm.example.kafka.domain.HotelInfo
import java.time.OffsetDateTime

class ReservationProcessing {

  companion object {
    const val KEY = "io.holunda.example.travel-agency.reservation-processing"
  }

  object Commands {
    const val BOOK_FLIGHT = "bookFlight"
    const val BOOK_HOTEL = "bookHotel"
  }

  object Elements {
    const val RESERVATION_RECEIVED = "reservation_received"
    const val FLIGHT_RECEIVED = "flight_reservation_received"
    const val HOTEL_RECEIVED = "hotel_reservation_received"
  }

  object Variables {
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
