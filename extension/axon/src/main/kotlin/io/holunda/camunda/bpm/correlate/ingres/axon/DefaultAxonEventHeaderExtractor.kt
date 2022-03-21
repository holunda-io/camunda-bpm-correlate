package io.holunda.camunda.bpm.correlate.ingres.axon

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_ID
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_TIMESTAMP
import io.holunda.camunda.bpm.data.CamundaBpmData
import org.axonframework.eventhandling.EventMessage

class DefaultAxonEventHeaderExtractor : AxonEventHeaderExtractor {

  override fun extractHeaders(eventMessage: EventMessage<*>): Map<String, Any> {
    return CamundaBpmData.builder()
      .set(HEADER_MESSAGE_ID, eventMessage.identifier)
      .set(HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME, eventMessage.payloadType.name)
      .set(HEADER_MESSAGE_TIMESTAMP, eventMessage.timestamp.toString())
      .build()
      .apply {
        // add all metadata headers
        this.putAll(eventMessage.metaData)
      }
  }
}
