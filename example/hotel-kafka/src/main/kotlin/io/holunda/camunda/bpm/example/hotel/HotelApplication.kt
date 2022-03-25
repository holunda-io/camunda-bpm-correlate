package io.holunda.camunda.bpm.example.hotel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<HotelApplication>(*args).let { Unit }

@SpringBootApplication
class HotelApplication {

  @Bean
  fun hotelService() = HotelService()

  @Bean
  fun objectMapper(): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}
