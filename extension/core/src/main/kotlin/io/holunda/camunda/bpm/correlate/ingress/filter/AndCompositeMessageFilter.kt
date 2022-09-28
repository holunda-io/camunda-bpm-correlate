package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Composite filter implementing the AND operator of all contained filters.
 */
class AndCompositeMessageFilter(
  private val filters: List<MessageFilter>
) : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean {
    return filters.all { filter -> filter.accepts(channelMessage, metaData) }
  }
}