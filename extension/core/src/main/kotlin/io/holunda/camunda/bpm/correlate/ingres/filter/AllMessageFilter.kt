package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage

/**
 * Accepts all messages.
 */
class AllMessageFilter : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean = true
}