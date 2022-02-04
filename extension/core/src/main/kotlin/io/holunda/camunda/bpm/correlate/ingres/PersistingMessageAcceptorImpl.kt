package io.holunda.camunda.bpm.correlate.ingres

import io.holunda.camunda.bpm.correlate.message.AbstractGenericMessage
import io.holunda.camunda.bpm.correlate.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import org.springframework.stereotype.Component

@Component
class PersistingMessageAcceptorImpl(
  private val messagePersistenceService: MessagePersistenceService,
  private val metadataExtractorChain: MessageMetadataExtractorChain
) : MessageAcceptor {

  override fun supports(headers: Map<String, Any>): Boolean {
    return metadataExtractorChain.supports(headers)
  }

  override fun <P> accept(message: AbstractGenericMessage<P>) {

    val messageMetaData = metadataExtractorChain.extractChainedMetaData(message)
    messagePersistenceService.persistMessage(message = message, metaData = messageMetaData)
  }
}
