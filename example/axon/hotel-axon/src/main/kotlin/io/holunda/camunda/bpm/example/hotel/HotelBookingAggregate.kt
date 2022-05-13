package io.holunda.camunda.bpm.example.hotel

import io.holunda.camunda.bpm.example.common.domain.hotel.BookHotelCommand
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelService
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class HotelBookingAggregate() {

  @AggregateIdentifier
  lateinit var id: String

  companion object {
    @JvmStatic
    @CommandHandler
    fun bookFlight(command: BookHotelCommand, hotelService: HotelService): HotelBookingAggregate = HotelBookingAggregate().apply {
      AggregateLifecycle.apply(hotelService.bookHotel(command))
    }
  }

  @EventSourcingHandler
  fun on(event: HotelReservationConfirmedEvent) {
    this.id = UUID.randomUUID().toString()
  }

}
