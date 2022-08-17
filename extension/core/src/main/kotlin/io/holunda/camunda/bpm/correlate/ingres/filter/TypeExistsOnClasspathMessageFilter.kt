package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.ChannelMessage
import org.springframework.util.ClassUtils

/**
 * Accepts all messages having payloads existing on the classpath.
 */
class TypeExistsOnClasspathMessageFilter : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean {
    return try {
      ClassUtils.resolveClassName(metaData.payloadTypeInfo.toFQCN(), null)
      true
    } catch (e: IllegalArgumentException) {
      false
    }
  }
}