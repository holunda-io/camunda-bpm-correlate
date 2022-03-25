package io.holunda.camunda.bpm.example.kafka.correlation.correlate

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEvent
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactory
import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelInfo
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.FLIGHT_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.HOTEL_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.RESERVATION_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DELAY
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DESTINATION
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DESTINATION_ARRIVAL_DATE
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DESTINATION_DEPARTURE_DATE
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.FLIGHT_INFO_INCOMING
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.FLIGHT_INFO_OUTGOING
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.HOTEL_INFO
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.RESERVATION_ID
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.SOURCE
import io.holunda.camunda.bpm.example.kafka.toProcessVariables

class ReservationProcessingEventFactory(
  singleMessageCorrelationStrategy: SingleMessageCorrelationStrategy
) : CamundaCorrelationEventFactory {

  private val correlationSelector = singleMessageCorrelationStrategy.correlationSelector()

  override fun create(message: CorrelationMessage): CamundaCorrelationEvent? {
    return when (val payload = message.payload) {
      is ReservationReceivedEvent -> CamundaCorrelationEvent(
        name = RESERVATION_RECEIVED,
        variables = payload.toProcessVariables(),
        correlationHint = correlationSelector.invoke(message)
      )
      is FlightReservationConfirmedEvent -> CamundaCorrelationEvent(
        name = FLIGHT_RECEIVED,
        variables = payload.toProcessVariables(),
        correlationHint = correlationSelector.invoke(message)
      )
      is HotelReservationConfirmedEvent -> CamundaCorrelationEvent(
        name = HOTEL_RECEIVED,
        variables = payload.toProcessVariables(),
        correlationHint = correlationSelector.invoke(message)
      )
      else -> null
    }
  }

  override fun supports(metaData: MessageMetaData): Boolean {
    return when (metaData.payloadTypeInfo.toFQCN()) {
      ReservationReceivedEvent::class.qualifiedName,
      HotelReservationConfirmedEvent::class.qualifiedName,
      FlightReservationConfirmedEvent::class.qualifiedName
      -> true
      else
      -> false
    }
  }
}
