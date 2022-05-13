package io.holunda.camunda.bpm.example.axon.correlate

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEvent
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactory
import io.holunda.camunda.bpm.example.axon.ReservationProcessing.Elements.FLIGHT_RECEIVED
import io.holunda.camunda.bpm.example.axon.ReservationProcessing.Elements.HOTEL_RECEIVED
import io.holunda.camunda.bpm.example.axon.ReservationProcessing.Elements.RESERVATION_RECEIVED
import io.holunda.camunda.bpm.example.axon.toProcessVariables
import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent

/**
 * Specify the state update.
 */
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
