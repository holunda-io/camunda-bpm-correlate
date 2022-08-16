package io.holunda.camunda.bpm.correlate.ingres

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Message filter to filter messages delivered to the message acceptor.
 */
interface MessageFilter {
  /**
   * Checks if the message should be delivered to the message acceptor.
   * @param message message instance
   * @param messageMetaData metadata of the message
   * @return `true` if message should be accepted.
   */
  fun <P> accepts(message: AbstractChannelMessage<P>, messageMetaData: MessageMetaData): Boolean
}