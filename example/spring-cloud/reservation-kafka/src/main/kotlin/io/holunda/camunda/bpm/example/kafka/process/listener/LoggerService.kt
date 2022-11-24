package io.holunda.camunda.bpm.example.kafka.process.listener

import mu.KLogging
import org.springframework.stereotype.Component

/**
 * Simple logger service.
 */
@Component("loggerService")
class LoggerService {

  companion object: KLogging()

  /**
   * Log a message.
   */
  fun log(message: String) {
    logger.info { "[CORRELATE-LOG]: $message" }
  }
}
