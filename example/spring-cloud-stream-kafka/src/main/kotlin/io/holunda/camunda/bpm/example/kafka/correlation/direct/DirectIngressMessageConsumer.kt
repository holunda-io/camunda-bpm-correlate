package io.holunda.camunda.bpm.example.kafka.correlation.direct

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME
import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.FLIGHT_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.HOTEL_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Elements.RESERVATION_RECEIVED
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.RESERVATION_ID
import io.holunda.camunda.bpm.example.kafka.toProcessVariables
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.messaging.Message
import java.util.function.Consumer

/**
 * Direct correlation.
 * The message is received and correlated directly using the supplied runtime service.
 */
class DirectIngressMessageConsumer(
  private val objectMapper: ObjectMapper,
  private val runtimeService: RuntimeService,
  private val repositoryService: RepositoryService
) : Consumer<Message<ByteArray>> {

  /**
   * Latest version of the RESERVATION process.
   */
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

  /**
   * Accepts the message from Kafka and correlates.
   * @param message message received from Kafka, encoded as ByteArray (JSON inside).
   */
  override fun accept(message: Message<ByteArray>) {
    when (val event = deserializeMessage(message)) {
      is ReservationReceivedEvent -> {
        runtimeService
          .createMessageCorrelation(RESERVATION_RECEIVED)                             // TARGETING: by message name
          .startMessageOnly()                                                         // TARGETING: start messages only
          .processDefinitionId(processDefinitionId)                                   // TARGETING: by process definition id
          .setVariables(event.toProcessVariables())                                   // STATE UPDATE
          .correlate()
      }
      is FlightReservationConfirmedEvent -> {
        runtimeService
          .createMessageCorrelation(FLIGHT_RECEIVED)                                  // TARGETING: message name
          .processInstanceVariableEquals(CUSTOMER_NAME.name, event.passengersName)    // TARGETING: process variable
          .processInstanceVariableEquals(RESERVATION_ID.name, event.bookingReference) // TARGETING: process variable
          .setVariables(event.toProcessVariables())                                   // STATE UPDATE
          .correlate()
      }
      is HotelReservationConfirmedEvent -> {
        runtimeService
          .createMessageCorrelation(HOTEL_RECEIVED)                                   // TARGETING: message name
          .processInstanceVariableEquals(CUSTOMER_NAME.name, event.guestName)         // TARGETING: process variable
          .processInstanceVariableEquals(RESERVATION_ID.name, event.bookingReference) // TARGETING: process variable
          .setVariables(event.toProcessVariables())                                   // STATE UPDATE
          .correlate()
      }
      else -> throw IllegalArgumentException("Could not correlate message of type ${event::class.java.canonicalName}")
    }
  }

  /**
   * Deserializes the JSON message to the typed message.
   * Use PAYLOAD_TYPE_CLASS_NAME to detect the type.
   * @param message bytearray JSON message.
   * @return typed message.
   */
  private fun deserializeMessage(message: Message<ByteArray>): Any {
    val typeFullQualifiedName =
      (message.headers.getValue(HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME.name) ?: throw IllegalArgumentException("Type must not be null.")) as ByteArray
    val type = objectMapper.typeFactory.constructFromCanonical(String(typeFullQualifiedName))
    return objectMapper.readValue(message.payload, type)
  }
}
