package io.holunda.camunda.bpm.example.kafka.process.delegate

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.example.common.domain.flight.BookFlightCommand
import io.holunda.camunda.bpm.example.common.domain.hotel.BookHotelCommand
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component

@Component
class CommandService(
  private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
  private val objectMapper: ObjectMapper
) {

  fun bookHotel(command: BookHotelCommand) {
    val payload = objectMapper.writeValueAsBytes(command)
    kafkaTemplate.send("hotel", payload)
  }

  fun bookFlight(command: BookFlightCommand) {
    val payload = objectMapper.writeValueAsBytes(command)
    kafkaTemplate.send("flight", payload)
  }
}
