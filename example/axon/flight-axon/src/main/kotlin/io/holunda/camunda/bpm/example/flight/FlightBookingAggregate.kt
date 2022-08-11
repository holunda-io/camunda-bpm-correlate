package io.holunda.camunda.bpm.example.flight

import io.holunda.camunda.bpm.example.common.domain.flight.BookFlightCommand
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightService
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class FlightBookingAggregate() {

  @AggregateIdentifier
  lateinit var id: String

  companion object {
    @JvmStatic
    @CommandHandler
    fun bookFlight(command: BookFlightCommand, flightService: FlightService): FlightBookingAggregate = FlightBookingAggregate().apply {
      AggregateLifecycle.apply(flightService.bookFlight(command))
    }
  }

  @EventSourcingHandler
  fun on(event: FlightReservationConfirmedEvent) {
    this.id = UUID.randomUUID().toString()
  }

}
