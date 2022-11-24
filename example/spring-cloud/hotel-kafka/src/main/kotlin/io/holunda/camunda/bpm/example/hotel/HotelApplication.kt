package io.holunda.camunda.bpm.example.hotel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

/**
 * Starts the app.
 */
fun main(args: Array<String>) = runApplication<HotelApplication>(*args).let { Unit }

/**
 * Hotel application.
 */
@SpringBootApplication
class HotelApplication {

  @Value("\${hotel.processing-delay:1}")
  private var delay: Long = 1

  /**
   * Hotel service.
   */
  @Bean
  fun hotelService() = HotelService(delay)

  /**
   * Object mapper.
   */
  @Bean
  fun objectMapper(): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}
