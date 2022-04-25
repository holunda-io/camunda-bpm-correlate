package io.holunda.camunda.bpm.example.common.domain.hotel

import io.holunda.camunda.bpm.example.common.domain.setHours
import mu.KLogging

class HotelService(
  private val delay: Long
) {
  companion object : KLogging()

  init {
    logger.info { "Hotel Service will delay processing by $delay seconds." }
  }

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
