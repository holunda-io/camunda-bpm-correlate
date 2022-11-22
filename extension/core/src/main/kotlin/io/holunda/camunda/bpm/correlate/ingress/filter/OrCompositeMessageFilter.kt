package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Composite filter implementing the OR operator of all contained filters.
 */
class OrCompositeMessageFilter(
  private val filters: List<MessageFilter>
) : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean {
    return filters.any { filter -> filter.accepts(channelMessage, metaData) }
  }
}