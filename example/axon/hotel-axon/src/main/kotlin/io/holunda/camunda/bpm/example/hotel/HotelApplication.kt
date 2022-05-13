package io.holunda.camunda.bpm.example.hotel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.WildcardTypePermission
import io.holunda.camunda.bpm.example.common.domain.flight.FlightService
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<FlightApplication>(*args).let{ Unit }

@SpringBootApplication
class FlightApplication {

  @Value("\${hotel#.processing-delay:1}")
  private var delay: Long = 1

  @Bean
  fun hotelService() = HotelService(delay = delay)

  @Bean
  fun objectMapper(): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  @Bean
  fun xStream() = XStream().apply {
    addPermission(WildcardTypePermission(arrayOf("io.holunda.camunda.bpm.example.**")))
  }
}
