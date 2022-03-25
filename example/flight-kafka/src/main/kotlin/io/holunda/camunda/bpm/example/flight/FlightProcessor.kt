package io.holunda.camunda.bpm.example.flight

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME
import io.holunda.camunda.bpm.example.common.domain.flight.BookFlightCommand
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightService
import mu.KLogging
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component

@Component
class FlightProcessor(
  val objectMapper: ObjectMapper,
  val flightService: FlightService
) : java.util.function.Function<Message<ByteArray>, Message<ByteArray>> {

  companion object : KLogging()

  override fun apply(message: Message<ByteArray>): Message<ByteArray> {

    val command = objectMapper.readValue<BookFlightCommand>(message.payload)

    logger.info { "Received command: $command" }
    val result = flightService.bookFlight(command)
    val type = FlightReservationConfirmedEvent::class.java.canonicalName
    logger.info { "Sending result: $result using type $type" }
    return MessageBuilder.createMessage(
      objectMapper.writeValueAsBytes(result),
      MessageHeaders(
        mapOf(
          HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME.name to type.toByteArray()
        )
      )
    )
  }

}
