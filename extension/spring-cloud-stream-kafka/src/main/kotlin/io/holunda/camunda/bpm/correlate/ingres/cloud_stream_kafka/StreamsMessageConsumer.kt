package io.holunda.camunda.bpm.correlate.ingres.cloud_stream_kafka

import io.holunda.camunda.bpm.correlate.ingres.ByteMessage
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.ingres.MessageAcceptor
import mu.KLogging
import org.springframework.messaging.Message
import java.util.function.Consumer


class StreamsMessageConsumer(
  private val messageAcceptor: MessageAcceptor,
  private val metrics: IngresMetrics
) : Consumer<Message<ByteArray>> {

  companion object : KLogging()

  override fun accept(message: Message<ByteArray>) {
    metrics.incrementReceived()
    if (messageAcceptor.supports(message.headers)) {
      messageAcceptor.accept(ByteMessage(headers = message.headers, payload = message.payload))
      logger.trace { "Accepted message ${message.headers}" }
      metrics.incrementAccepted()
    } else {
      logger.trace { "Ignored message ${message.headers}, it is not supported by client." }
      metrics.incrementIgnored()
    }
  }
}
