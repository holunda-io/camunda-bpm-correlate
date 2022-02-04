package io.holunda.camunda.bpm.correlate.ingres.cloudstream

import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.ingres.MessageAcceptor
import io.holunda.camunda.bpm.correlate.message.JsonMessage
import mu.KLogging
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import java.util.function.Consumer

/**
 * Spring cloud stream message consumer.
 */
class StreamJsonMessageConsumer(
  private val messageAcceptor: MessageAcceptor,
  private val metrics: IngresMetrics
) : Consumer<Message<String>> {

  companion object : KLogging()

  override fun accept(message: Message<String>) {
    metrics.incrementReceived()
    val headers = convertHeaders(message.headers)
    if (messageAcceptor.supports(headers)) {
      messageAcceptor.accept(JsonMessage(headers = headers, payload = message.payload))
      logger.trace { "Accepted message $headers" }
      metrics.incrementAccepted()
    } else {
      logger.trace { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored()
    }
  }

  /**
   * Converts channel headers to correlate headers.
   */
  private fun convertHeaders(headers: MessageHeaders): Map<String, Any> {
    // FIXME: plugable converter
    return headers
  }
}
