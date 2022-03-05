package io.holunda.camunda.bpm.example.kafka.process.delegate

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
class BookFlightDelegate: JavaDelegate {

  companion object: KLogging()

  override fun execute(execution: DelegateExecution) {
    logger.info("Should book flight")
  }
}
