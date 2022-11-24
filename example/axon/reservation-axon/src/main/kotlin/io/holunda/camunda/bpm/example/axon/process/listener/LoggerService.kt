package io.holunda.camunda.bpm.example.axon.process.listener

import mu.KLogging
import org.springframework.stereotype.Component

/**
 * Simple logger service.
 */
@Component("loggerService")
class LoggerService {

  companion object: KLogging()

  /**
   * Logs a message.
   */
  fun log(message: String) {
    logger.info { "[CORRELATE-LOG]: $message" }
  }
}
