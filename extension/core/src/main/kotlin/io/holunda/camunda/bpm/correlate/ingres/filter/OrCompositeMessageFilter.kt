package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Composite filter implementing the OR operator of all contained filters.
 */
class OrCompositeMessageFilter(
  private val filters: List<MessageFilter>
) : MessageFilter {
  override fun <P> accepts(message: AbstractChannelMessage<P>, messageMetaData: MessageMetaData): Boolean {
    return filters.any { filter -> filter.accepts(message, messageMetaData) }
  }
}