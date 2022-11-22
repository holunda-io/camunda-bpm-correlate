package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Accepts all messages having payloads of specified types.
 */
class TypeListMessageFilter(
  private vararg val fullQualifiedTypeNames: String
) : MessageFilter {

  constructor(types: Set<Class<*>>) : this(* types.mapNotNull { type -> type.canonicalName }.toTypedArray())

  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean {
    return fullQualifiedTypeNames.contains(metaData.payloadTypeInfo.toFQCN())
  }
}