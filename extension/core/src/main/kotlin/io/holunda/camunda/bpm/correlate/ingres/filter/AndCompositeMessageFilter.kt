package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Composite filter implementing the AND operator of all contained filters.
 */
class AndCompositeMessageFilter(
  private val filters: List<MessageFilter>
) : MessageFilter {
  override fun <P> accepts(message: AbstractChannelMessage<P>, messageMetaData: MessageMetaData): Boolean {
    return filters.all { filter -> filter.accepts(message, messageMetaData) }
  }
}