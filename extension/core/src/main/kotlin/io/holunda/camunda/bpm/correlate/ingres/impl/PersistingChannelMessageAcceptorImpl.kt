package io.holunda.camunda.bpm.correlate.ingres.impl

import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.persist.impl.DefaultMessagePersistenceService

/**
 * Acceptor persisting the message.
 */
class PersistingChannelMessageAcceptorImpl(
  private val messagePersistenceService: DefaultMessagePersistenceService,
  private val metadataExtractorChain: MessageMetadataExtractorChain
) : ChannelMessageAcceptor {

  override fun supports(headers: Map<String, Any>): Boolean {
    return metadataExtractorChain.supports(headers)
  }

  override fun <P> accept(message: AbstractChannelMessage<P>) {
    val messageMetaData = metadataExtractorChain.extractChainedMetaData(message)
    messagePersistenceService.persistMessage(channelMessage = message, metaData = messageMetaData)
  }
}
