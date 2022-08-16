package io.holunda.camunda.bpm.correlate.ingres.cloudstream

import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.ingres.message.ByteMessage
import mu.KLogging
import org.springframework.messaging.Message
import java.util.function.Consumer

/**
 * Spring cloud stream message consumer.
 */
class StreamByteMessageConsumer(
  private val messageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngresMetrics,
  private val channelMessageHeaderConverter: ChannelMessageHeaderConverter
) : Consumer<Message<ByteArray>> {

  companion object : KLogging()

  override fun accept(message: Message<ByteArray>) {
    metrics.incrementReceived()
    val headers = channelMessageHeaderConverter.extractMessageHeaders(message)
    if (messageAcceptor.supports(headers)) {
      messageAcceptor.accept(ByteMessage(headers = headers, payload = message.payload))
      logger.trace { "Accepted message $headers" }
      metrics.incrementAccepted()
    } else {
      logger.warn { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored()
    }
  }

}
