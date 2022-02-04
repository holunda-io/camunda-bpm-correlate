package io.holunda.camunda.bpm.example.kafka

import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.customVariable
import io.holunda.camunda.bpm.data.CamundaBpmDataKotlin.stringVariable
import io.holunda.camunda.bpm.example.kafka.domain.FlightInfo
import io.holunda.camunda.bpm.example.kafka.domain.HotelInfo
import mu.KLogging
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class ReservationProcessingFactoryBean {

  companion object: KLogging() {
    const val KEY = "io.holunda.example.travel-agency.reservation-processing"
  }

  object Elements {
    const val RESERVATION_RECEIVED = "reservation_received"
    const val FLIGHT_RECEIVED = "flight_reservation_received"
    const val HOTEL_RECEIVED = "hotel_reservation_received"
  }

  object Variables {
    val RESERVATION_ID = stringVariable("reservationId")
    val CUSTOMER_NAME = stringVariable("customerName")
    val ARRIVAL_DATE = customVariable<OffsetDateTime>("arrivalDate")
    val DEPARTURE_DATE = customVariable<OffsetDateTime>("departureDate")
    val SOURCE = stringVariable("source")
    val DESTINATION = stringVariable("destination")

    val HOTEL_INFO = customVariable<HotelInfo>("hotelInfo")
    val FLIGHT_INFO_OUTGOING = customVariable<FlightInfo>("flightInfoOutgoing")
    val FLIGHT_INFO_INCOMING = customVariable<FlightInfo>("flightInfoIncoming")
  }
}
