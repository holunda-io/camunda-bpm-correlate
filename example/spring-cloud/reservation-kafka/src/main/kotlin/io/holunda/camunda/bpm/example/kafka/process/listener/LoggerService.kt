package io.holunda.camunda.bpm.example.kafka.process.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}
/**
 * Simple logger service.
 */
@Component("loggerService")
class LoggerService {

  /**
   * Log a message.
   */
  fun log(message: String) {
    logger.info { "[CORRELATE-LOG]: $message" }
  }
}
