package io.holunda.camunda.bpm.correlate.ingres.impl

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.MessageFilter
import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService

/**
 * Acceptor persisting the message.
 */
class PersistingChannelMessageAcceptorImpl(
  private val messagePersistenceService: MessagePersistenceService,
  private val messageMetadataExtractorChain: MessageMetadataExtractorChain,
  private val messageFilter: MessageFilter
) : ChannelMessageAcceptor {

  override fun supports(headers: Map<String, Any>): Boolean {
    return messageMetadataExtractorChain.supports(headers)
  }

  override fun <P> accept(message: AbstractChannelMessage<P>) {
    val messageMetaData = messageMetadataExtractorChain.extractChainedMetaData(message)
    if (messageFilter.accepts(message, messageMetaData)) {
      messagePersistenceService.persistMessage(channelMessage = message, metaData = messageMetaData)
      // TODO: metrics
    } else {
      // TODO: metrics
    }
  }
}
