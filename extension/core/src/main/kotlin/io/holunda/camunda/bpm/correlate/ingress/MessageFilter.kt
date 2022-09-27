package io.holunda.camunda.bpm.correlate.ingress

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Message filter to filter messages delivered to the message acceptor.
 */
interface MessageFilter {
  /**
   * Checks if the message should be delivered to the message acceptor.
   * @param channelMessage message instance
   * @param metaData metadata of the message
   * @return `true` if message should be accepted.
   */
  fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean
}