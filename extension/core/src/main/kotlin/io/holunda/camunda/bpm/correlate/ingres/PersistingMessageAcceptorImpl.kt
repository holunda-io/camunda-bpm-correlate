package io.holunda.camunda.bpm.correlate.ingres

import io.holunda.camunda.bpm.correlate.ingres.message.AbstractChannelMessage
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import org.springframework.stereotype.Component

/**
 * Acceptor persisting the message.
 */
class PersistingMessageAcceptorImpl(
  private val messagePersistenceService: MessagePersistenceService,
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
