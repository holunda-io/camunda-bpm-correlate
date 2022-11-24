package io.holunda.camunda.bpm.example.hotel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.holunda.camunda.bpm.example.common.domain.hotel.BookHotelCommand
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelService
import mu.KLogging
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component

/**
 * Hotel processor, typical Spring Cloud Function implementation.
 */
@Component
class HotelProcessor(
  private val objectMapper: ObjectMapper,
  private val hotelService: HotelService
) : java.util.function.Function<Message<ByteArray>, Message<ByteArray>> {

  companion object : KLogging()

  override fun apply(message: Message<ByteArray>): Message<ByteArray> {
    val command = objectMapper.readValue<BookHotelCommand>(message.payload)
    logger.info { "Received command: $command" }
    val result = hotelService.bookHotel(command)
    logger.info { "Sending result: $result" }
    return MessageBuilder.createMessage(
      objectMapper.writeValueAsBytes(result),
      MessageHeaders(
        mapOf(
          "X-CORRELATE-PayloadType-FQCN" to HotelReservationConfirmedEvent::class.java.canonicalName.toByteArray()
        )
      )
    )
  }

}
