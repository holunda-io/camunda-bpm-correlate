package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Accepts all messages.
 */
class AllMessageFilter : MessageFilter {
  override fun <P> accepts(message: AbstractChannelMessage<P>, messageMetaData: MessageMetaData): Boolean = true
}