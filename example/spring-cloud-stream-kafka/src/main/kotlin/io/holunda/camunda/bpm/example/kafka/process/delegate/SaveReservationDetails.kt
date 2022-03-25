package io.holunda.camunda.bpm.example.kafka.process.delegate

import io.holunda.camunda.bpm.data.CamundaBpmData.reader
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.DELAY
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.RESERVATION_ID
import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
class SaveReservationDetails: JavaDelegate {

  companion object: KLogging()

  override fun execute(execution: DelegateExecution) {
    val reader = reader(execution)
    logger.warn { "Saving reservation (${reader.get(RESERVATION_ID)}) details for customer ${reader.get(CUSTOMER_NAME)}." }
    busyWait(reader.get(DELAY))
    logger.warn { "Reservation (${reader.get(RESERVATION_ID)}) details saved." }
  }

  private fun busyWait(delay: Long) {
    for (i in 0.. delay) {
      logger.warn { "Saving in progress ${i}/${delay}." }
      Thread.sleep(1000)
    }
  }
}
