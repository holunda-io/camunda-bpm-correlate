package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.ingress.message.ByteMessage
import mu.KLogging
import org.springframework.messaging.Message
import java.util.function.Consumer

/**
 * Spring cloud stream message consumer.
 */
class StreamByteMessageConsumer(
  private val messageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngressMetrics,
  private val channelMessageHeaderConverter: ChannelMessageHeaderConverter
) : Consumer<Message<ByteArray>> {

  companion object : KLogging()

  override fun accept(message: Message<ByteArray>) {
    metrics.incrementReceived()
    val headers = channelMessageHeaderConverter.extractMessageHeaders(message)
    logger.debug { "Received message $headers" }
    if (messageAcceptor.supports(headers)) {
      messageAcceptor.accept(ByteMessage(headers = headers, payload = message.payload))
      logger.debug { "Accepted message $headers" }
      metrics.incrementAccepted()
    } else {
      logger.debug { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored()
    }
  }

}
