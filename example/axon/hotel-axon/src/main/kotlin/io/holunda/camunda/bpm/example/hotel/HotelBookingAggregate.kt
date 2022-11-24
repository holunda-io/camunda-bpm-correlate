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

/**
 * Aggregate representing the hotel.
 */
@Aggregate
class HotelBookingAggregate {

  @AggregateIdentifier
  lateinit var id: String

  companion object {
    /**
     * Hotel command handler.
     */
    @JvmStatic
    @CommandHandler
    fun bookHotel(command: BookHotelCommand, hotelService: HotelService): HotelBookingAggregate = HotelBookingAggregate().apply {
      AggregateLifecycle.apply(hotelService.bookHotel(command))
    }
  }

  /**
   * Event sourcing handler.
   */
  @EventSourcingHandler
  fun on(event: HotelReservationConfirmedEvent) {
    this.id = UUID.randomUUID().toString()
  }

}
