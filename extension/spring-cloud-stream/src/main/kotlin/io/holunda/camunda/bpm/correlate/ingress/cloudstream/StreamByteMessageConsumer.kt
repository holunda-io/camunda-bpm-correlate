package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.ingress.cloudstream.SpringCloudStreamChannelConfiguration.Companion.CHANNEL_TYPE
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
  val streamChannelMessageHeaderConverter: StreamChannelMessageHeaderConverter,
  val channelName: String
) : Consumer<Message<ByteArray>> {

  companion object : KLogging()

  override fun accept(message: Message<ByteArray>) {
    metrics.incrementReceived(channelName, CHANNEL_TYPE)
    val headers = streamChannelMessageHeaderConverter.extractMessageHeaders(message)
    logger.debug { "Received message $headers" }
    if (messageAcceptor.supports(headers)) {
      messageAcceptor.accept(ByteMessage(headers = headers, payload = message.payload))
      logger.debug { "Accepted message $headers" }
      metrics.incrementAccepted(channelName, CHANNEL_TYPE)
    } else {
      logger.debug { "Ignored message $headers, it is not supported by client." }
      metrics.incrementIgnored(channelName, CHANNEL_TYPE)
    }
  }

}
