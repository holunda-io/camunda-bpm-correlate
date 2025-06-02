package io.holunda.camunda.bpm.example.kafka

import jakarta.annotation.PostConstruct
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

/**
 * Starts the app.
 */
fun main(args: Array<String>) = runApplication<TravelAgencyKafkaCorrelationApplication>(*args).let { Unit }

/**
 * Reservation application.
 */
@SpringBootApplication
@EnableProcessApplication
class TravelAgencyKafkaCorrelationApplication {

  /**
   * Sets time to UTC.
   */
  @PostConstruct
  fun init() {
    // Setting Spring Boot SetTimeZone
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
  }

}
