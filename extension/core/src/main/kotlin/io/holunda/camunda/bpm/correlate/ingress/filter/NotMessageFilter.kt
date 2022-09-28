package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Filter implementing the NOT operator.
 */
class NotMessageFilter(
  private val filter: MessageFilter
) : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean {
    return !filter.accepts(channelMessage, metaData)
  }
}