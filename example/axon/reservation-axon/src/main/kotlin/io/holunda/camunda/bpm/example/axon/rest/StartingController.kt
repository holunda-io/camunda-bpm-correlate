package io.holunda.camunda.bpm.example.axon.rest

import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

/**
 * Simple controller allowing to start reservations via REST.
 */
@RestController
@RequestMapping("/process")
class StartingController(
  val eventGateway: EventGateway
) {

  /**
   * Starts the process via REST.
   */
  @PostMapping("/reservation")
  fun reservationReceived(@NonNull @RequestBody request: ReservationReceivedEvent): ResponseEntity<Void> {
    logger.info { "Reservation received, publishing corresponding event $request" }
    eventGateway.publish(request)
    return noContent().build()
  }
}
