package io.holunda.camunda.bpm.example.axon.process.delegate

import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.example.axon.ReservationProcessing
import io.holunda.camunda.bpm.example.common.domain.hotel.BookHotelCommand
import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}
/**
 * Delegate sending the command to Axon Hotel Service.
 */
@Component
class BookHotelDelegate(
  val commandGateway: CommandGateway
) : JavaDelegate {

  override fun execute(execution: DelegateExecution) {
    val reader = reader(execution)
    val command = BookHotelCommand(
      guestName = reader.get(ReservationProcessing.Variables.CUSTOMER_NAME),
      checkin = reader.get(ReservationProcessing.Variables.DESTINATION_ARRIVAL_DATE),
      checkout = reader.get(ReservationProcessing.Variables.DESTINATION_DEPARTURE_DATE),
      bookingReference = reader.get(ReservationProcessing.Variables.RESERVATION_ID)
    )
    commandGateway.sendAndWait<Void>(command)
    logger.info { "[SEND BOOK HOTEL] Book hotel sent for ${reader.get(ReservationProcessing.Variables.RESERVATION_ID)}." }
  }
}
