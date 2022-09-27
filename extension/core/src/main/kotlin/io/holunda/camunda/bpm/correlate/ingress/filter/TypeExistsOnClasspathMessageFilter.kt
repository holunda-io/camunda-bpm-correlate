package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage
import org.springframework.util.ClassUtils

/**
 * Accepts all messages having payloads existing on the classpath.
 */
class TypeExistsOnClasspathMessageFilter(
  private val classLoader: ClassLoader? = null
) : MessageFilter {
  override fun <P> accepts(channelMessage: ChannelMessage<P>, metaData: MessageMetaData): Boolean {
    return try {
      ClassUtils.resolveClassName(metaData.payloadTypeInfo.toFQCN(), classLoader)
      true
    } catch (e: IllegalArgumentException) {
      false
    }
  }
}