package io.holunda.camunda.bpm.example.axon.process.delegate

import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.example.axon.ReservationProcessing
import io.holunda.camunda.bpm.example.common.domain.flight.BookFlightCommand
import mu.KLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
class BookFlightDelegate(
  val commandGateway: CommandGateway
) : JavaDelegate {

  companion object : KLogging()

  override fun execute(execution: DelegateExecution) {
    val reader = reader(execution)
    val command = BookFlightCommand(
      passengersName = reader.get(ReservationProcessing.Variables.CUSTOMER_NAME),
      sourceCity = reader.get(ReservationProcessing.Variables.SOURCE),
      destinationCity = reader.get(ReservationProcessing.Variables.DESTINATION),
      destinationArrivalDate = reader.get(ReservationProcessing.Variables.DESTINATION_ARRIVAL_DATE),
      destinationDepartureDate = reader.get(ReservationProcessing.Variables.DESTINATION_DEPARTURE_DATE),
      bookingReference = reader.get(ReservationProcessing.Variables.RESERVATION_ID)
    )
    commandGateway.sendAndWait<Void>(command)
    logger.info("[SEND BOOK FLIGHT] Book flight sent for ${reader.get(ReservationProcessing.Variables.RESERVATION_ID)}.")
  }
}
