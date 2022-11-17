package io.holunda.camunda.bpm.correlate.ingress.axon

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_ID
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.Companion.HEADER_MESSAGE_TIMESTAMP
import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import org.axonframework.eventhandling.EventMessage

/**
 * Default Axon Event Message Header Extractor.
 * Retrieves from Axon Event Message:
 *  - message-id
 *  - payload-type-class-name
 *  - message-timestamp
 * These headers will make the [HeaderMessageMetaDataSnippetExtractor] happy.
 * In addition, it adds all metadata headers as message headers.
 */
class DefaultAxonEventMessageHeaderConverter : AxonEventMessageHeaderConverter {

  override fun extractHeaders(eventMessage: EventMessage<*>): Map<String, Any> {
    return builder()
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
