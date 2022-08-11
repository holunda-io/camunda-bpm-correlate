package io.holunda.camunda.bpm.example.kafka.process.delegate

import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.example.common.domain.hotel.BookHotelCommand
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing
import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
class BookHotelDelegate(
  val commandService: CommandService
) : JavaDelegate {

  companion object : KLogging()

  override fun execute(execution: DelegateExecution) {
    val reader = reader(execution)
    val command = BookHotelCommand(
      guestName = reader.get(ReservationProcessing.Variables.CUSTOMER_NAME),
      checkin = reader.get(ReservationProcessing.Variables.DESTINATION_ARRIVAL_DATE),
      checkout = reader.get(ReservationProcessing.Variables.DESTINATION_DEPARTURE_DATE),
      bookingReference = reader.get(ReservationProcessing.Variables.RESERVATION_ID)
    )
    commandService.bookHotel(command)
    logger.info { "[SEND BOOK HOTEL] Book hotel sent for ${reader.get(ReservationProcessing.Variables.RESERVATION_ID)}." }
  }
}
