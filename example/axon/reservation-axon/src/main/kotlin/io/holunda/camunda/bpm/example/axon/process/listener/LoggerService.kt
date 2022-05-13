package io.holunda.camunda.bpm.example.axon.process.listener

import mu.KLogging
import org.springframework.stereotype.Component

@Component("loggerService")
class LoggerService {

  companion object: KLogging()

  fun log(message: String) {
    logger.info { "[CORRELATE-LOG]: $message" }
  }
}
