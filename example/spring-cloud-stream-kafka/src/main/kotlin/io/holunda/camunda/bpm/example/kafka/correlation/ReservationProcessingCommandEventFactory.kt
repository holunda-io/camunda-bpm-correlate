package io.holunda.camunda.bpm.example.kafka.correlation

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEvent
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactory
import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Commands.BOOK_FLIGHT
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Commands.BOOK_HOTEL
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.FLIGHT_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.HOTEL_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.RESERVATION_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DESTINATION
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DESTINATION_ARRIVAL_DATE
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DESTINATION_DEPARTURE_DATE
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.FLIGHT_INFO_INCOMING
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.FLIGHT_INFO_OUTGOING
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.HOTEL_INFO
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.RESERVATION_ID
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.SOURCE
import io.holunda.camunda.bpm.example.kafka.domain.*
import org.camunda.bpm.engine.variable.VariableMap
import org.springframework.stereotype.Component

@Component
class ReservationProcessingCommandEventFactory(
  reservationProcessingSingleMessageCorrelationStrategy: ReservationProcessingCorrelation
) : CamundaCorrelationEventFactory {

  private val correlationSelector = reservationProcessingSingleMessageCorrelationStrategy.correlationSelector()

  override fun create(message: CorrelationMessage): CamundaCorrelationEvent? {
    val builder = builder()
    return when (val payload = message.payload) {
      is ReservationReceivedEvent -> CamundaCorrelationEvent(
        name = RESERVATION_RECEIVED,
        variables = builder
          .set(RESERVATION_ID, payload.reservationId)
          .set(CUSTOMER_NAME, payload.customerName)
          .set(SOURCE, payload.fromCity)
          .set(DESTINATION, payload.toCity)
          .set(DESTINATION_ARRIVAL_DATE, payload.from)
          .set(DESTINATION_DEPARTURE_DATE, payload.to)
          .build(),
        correlationHint = correlationSelector.invoke(message)
      )
      is FlightReservationReceivedEvent -> CamundaCorrelationEvent(
        name = FLIGHT_RECEIVED,
        variables = builder
          .set(FLIGHT_INFO_INCOMING, payload.incomingFlight)
          .set(FLIGHT_INFO_OUTGOING, payload.outgoingFlight)
          .build(),
        correlationHint = correlationSelector.invoke(message)
      )
      is HotelReservationReceivedEvent -> CamundaCorrelationEvent(
        name = HOTEL_RECEIVED,
        variables = builder
          .set(
            HOTEL_INFO,
            HotelInfo(
              customerName = payload.guestName,
              checkin = payload.arrival,
              checkout = payload.departure,
              roomNumber = payload.roomNumber
            )
          )
          .build(),
        correlationHint = correlationSelector.invoke(message)
      )
      else -> null
    }
  }

  fun command(commandName: String, variableMap: VariableMap): Any? {
    val reader = reader(variableMap)
    return when (commandName) {
      BOOK_FLIGHT -> {
        BookFlightCommand(
          passengersName = reader.get(CUSTOMER_NAME),
          source = reader.get(SOURCE),
          destinationAirport = reader.get(DESTINATION),
          destinationArrivalDate = reader.get(DESTINATION_ARRIVAL_DATE),
          destinationDepartureDate = reader.get(DESTINATION_DEPARTURE_DATE)
        )
      }
      BOOK_HOTEL -> {
        BookHotelCommand(
          guestName = reader.get(CUSTOMER_NAME),
          checkin = reader.get(DESTINATION_ARRIVAL_DATE),
          checkout = reader.get(DESTINATION_DEPARTURE_DATE),
        )
      }
      else -> null
    }
  }


  override fun supports(metaData: MessageMetaData): Boolean {
    return when (metaData.payloadTypeInfo.toFQCN()) {
      ReservationReceivedEvent::class.qualifiedName,
      HotelReservationReceivedEvent::class.qualifiedName,
      FlightReservationReceivedEvent::class.qualifiedName
      -> true
      else
      -> false
    }
  }
}
