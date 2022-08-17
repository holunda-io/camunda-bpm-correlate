package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage

/**
 * Accepts all messages having payloads of specified types.
 */
class TypeListMessageFilter(
  private vararg val fullQualifiedTypeNames: String
) : MessageFilter {

  constructor(types: Set<Class<*>>) : this(* types.map { type -> type.canonicalName }.toTypedArray())

  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean {
    return fullQualifiedTypeNames.contains(metaData.payloadTypeInfo.toFQCN())
  }
}