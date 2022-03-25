package io.holunda.camunda.bpm.example.common.domain.hotel

import io.holunda.camunda.bpm.example.common.domain.flight.setHours

class HotelService {

  fun bookHotel(command: BookHotelCommand): HotelReservationConfirmedEvent {

    return HotelReservationConfirmedEvent(
      guestName = command.guestName,
      bookingReference = command.bookingReference,
      checkin = command.checkin.setHours(15),
      checkout = command.checkout.setHours(12),
      roomNumber = "27"
    )
  }
}
