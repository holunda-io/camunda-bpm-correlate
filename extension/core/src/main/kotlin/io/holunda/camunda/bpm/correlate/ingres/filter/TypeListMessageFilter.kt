package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage

/**
 * Accepts all messages having payloads of specified types.
 */
class TypeListMessageFilter(
  private val types: Set<Class<Any>>
) : MessageFilter {
  override fun <P> accepts(message: AbstractChannelMessage<P>, messageMetaData: MessageMetaData): Boolean {
    return types.map { type -> type.canonicalName }.contains( messageMetaData.payloadTypeInfo.toFQCN() )
  }
}