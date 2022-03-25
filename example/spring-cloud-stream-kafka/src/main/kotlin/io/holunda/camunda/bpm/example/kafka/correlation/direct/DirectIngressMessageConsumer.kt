package io.holunda.camunda.bpm.example.kafka.correlation.direct

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME
import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.FLIGHT_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.RESERVATION_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.RESERVATION_ID
import io.holunda.camunda.bpm.example.kafka.toProcessVariables
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.messaging.Message
import java.util.function.Consumer

class DirectIngressMessageConsumer(
  private val objectMapper: ObjectMapper,
  private val runtimeService: RuntimeService,
  private val repositoryService: RepositoryService
) : Consumer<Message<ByteArray>> {

  private val processDefinitionId by lazy {
    requireNotNull(
      repositoryService
        .createProcessDefinitionQuery()
        .processDefinitionKey(ReservationProcessing.KEY)
        .active()
        .latestVersion()
        .singleResult()
    ) { "Reservation process with key ${ReservationProcessing.KEY} is not deployed" }.id
  }


  override fun accept(message: Message<ByteArray>) {
    when(val event = deserializeMessage(message)) {
      is ReservationReceivedEvent -> {
        runtimeService
          .createMessageCorrelation(RESERVATION_RECEIVED)
          .processDefinitionId(processDefinitionId)
          .setVariables(event.toProcessVariables())
          .correlate()
      }
      is FlightReservationConfirmedEvent -> {
        runtimeService
          .createMessageCorrelation(FLIGHT_RECEIVED)
          .processInstanceVariableEquals(CUSTOMER_NAME.name, event.passengersName)
          .processInstanceVariableEquals(RESERVATION_ID.name, event.bookingReference)
          .setVariables(event.toProcessVariables())
          .correlate()
      }
      is HotelReservationConfirmedEvent -> {
        runtimeService
          .createMessageCorrelation(FLIGHT_RECEIVED)
          .processInstanceVariableEquals(CUSTOMER_NAME.name, event.guestName)
          .processInstanceVariableEquals(RESERVATION_ID.name, event.bookingReference)
          .setVariables(event.toProcessVariables())
          .correlate()
      }
      else -> throw IllegalArgumentException("Could not correlate message of type ${event::class.java.canonicalName}")
    }
  }

  private fun deserializeMessage(message: Message<ByteArray>): Any {
    val typeFullQualifiedName =
      (message.headers.getValue(HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME.name) ?: throw IllegalArgumentException("Type must not be null.")) as String
    val type = objectMapper.typeFactory.constructFromCanonical(typeFullQualifiedName)
    return objectMapper.readValue(message.payload, type)
  }
}
