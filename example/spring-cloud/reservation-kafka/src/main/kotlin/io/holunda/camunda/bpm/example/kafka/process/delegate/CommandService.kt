package io.holunda.camunda.bpm.example.kafka.process.delegate

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.example.common.domain.flight.BookFlightCommand
import io.holunda.camunda.bpm.example.common.domain.hotel.BookHotelCommand
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component

/**
 * Kafka adapter sending the command via Kafka.
 */
@Component
class CommandService(
  private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
  private val objectMapper: ObjectMapper
) {

  /**
   * Send book hotel command via Kafka.
   */
  fun bookHotel(command: BookHotelCommand) {
    val payload = objectMapper.writeValueAsBytes(command)
    kafkaTemplate.send("hotel", payload)
  }

  /**
   * Send book flight command via Kafka.
   */
  fun bookFlight(command: BookFlightCommand) {
    val payload = objectMapper.writeValueAsBytes(command)
    kafkaTemplate.send("flight", payload)
  }
}
