package io.holunda.camunda.bpm.example.common.domain.hotel

import io.holunda.camunda.bpm.example.common.domain.setHours
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}
/**
 * Hotel service.
 */
class HotelService(
  private val delay: Long
) {

  init {
    logger.info { "Hotel Service will delay processing by $delay seconds." }
  }

  /**
   * Booking of the hotel.
   */
  fun bookHotel(command: BookHotelCommand): HotelReservationConfirmedEvent {

    for (i in 0..delay) {
      Thread.sleep(1000)
      logger.info { "$i / $delay" }
    }

    return HotelReservationConfirmedEvent(
      guestName = command.guestName,
      bookingReference = command.bookingReference,
      checkin = command.checkin.setHours(15),
      checkout = command.checkout.setHours(12),
      roomNumber = "27"
    )
  }
}
