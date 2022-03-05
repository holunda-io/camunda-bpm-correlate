package io.holunda.camunda.bpm.example.kafka.correlation

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEvent
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactory
import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Elements.FLIGHT_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Elements.HOTEL_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Elements.RESERVATION_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.ARRIVAL_DATE
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.DEPARTURE_DATE
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.DESTINATION
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.FLIGHT_INFO_INCOMING
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.FLIGHT_INFO_OUTGOING
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.HOTEL_INFO
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.RESERVATION_ID
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.SOURCE
import io.holunda.camunda.bpm.example.kafka.domain.FlightReservationReceivedEvent
import io.holunda.camunda.bpm.example.kafka.domain.HotelInfo
import io.holunda.camunda.bpm.example.kafka.domain.HotelReservationReceivedEvent
import io.holunda.camunda.bpm.example.kafka.domain.ReservationReceivedEvent
import org.springframework.stereotype.Component

@Component
class ReservationProcessingCamundaCorrelationEventFactory(
  reservationProcessingSingleMessageCorrelationStrategy: ReservationProcessingSingleMessageCorrelationStrategy
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
          .set(ARRIVAL_DATE, payload.from)
          .set(DEPARTURE_DATE, payload.to)
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
