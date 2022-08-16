package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Filter implementing the NOT operator.
 */
class NotMessageFilter(
  private val filter: MessageFilter
) : MessageFilter {
  override fun <P> accepts(message: AbstractChannelMessage<P>, messageMetaData: MessageMetaData): Boolean {
    return !filter.accepts(message, messageMetaData)
  }
}