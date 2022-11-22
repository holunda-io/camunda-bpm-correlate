package io.holunda.camunda.bpm.correlate.ingress.impl

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import io.holunda.camunda.bpm.correlate.util.ComponentLike

/**
 * Acceptor persisting the message.
 */
@ComponentLike
class PersistingChannelMessageAcceptorImpl(
  private val messagePersistenceService: MessagePersistenceService,
  private val messageMetadataExtractorChain: MessageMetadataExtractorChain,
  private val messageFilter: MessageFilter,
  private val ingressMetrics: IngressMetrics
) : ChannelMessageAcceptor {

  override fun supports(headers: Map<String, Any>): Boolean {
    return messageMetadataExtractorChain.supports(headers)
  }

  override fun <P> accept(message: ChannelMessage<P>) {
    val messageMetaData: MessageMetaData = messageMetadataExtractorChain.extractChainedMetaData(message)
    if (messageFilter.accepts(channelMessage = message, metaData = messageMetaData)) {
      messagePersistenceService.persistMessage(channelMessage = message, metaData = messageMetaData)
      ingressMetrics.incrementPersisted()
    } else {
      ingressMetrics.incrementDropped()
    }
  }
}
