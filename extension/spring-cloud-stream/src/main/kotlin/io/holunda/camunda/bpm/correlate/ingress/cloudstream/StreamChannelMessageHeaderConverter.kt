package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import org.springframework.messaging.Message

/**
 * Converts channel headers into message headers.
 */
interface StreamChannelMessageHeaderConverter {

  /**
   * Convert channel headers into generic message headers.
   * @param message message received by Spring Cloud.
   * @return headers as expected by acceptor.
   */
  fun extractMessageHeaders(message: Message<ByteArray>): Map<String, Any>
}
