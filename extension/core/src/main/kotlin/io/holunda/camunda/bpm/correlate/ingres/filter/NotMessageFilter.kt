package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage

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