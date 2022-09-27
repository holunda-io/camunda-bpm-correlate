package io.holunda.camunda.bpm.correlate.ingress.message

import io.holunda.camunda.bpm.correlate.LazyLoadingMessage
import io.holunda.camunda.bpm.correlate.PayloadType
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor.HeaderNames
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.ingress.filter.TypeListMessageFilter
import io.holunda.camunda.bpm.correlate.ingress.impl.PersistingChannelMessageAcceptorImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

/**
 * Test to simulate the usage of delegation message for Axon Framework use case.
 */
internal class DelegatingChannelMessageTest {

  private val payloadValue = PayloadType()

  private val payload: LazyLoadingMessage<PayloadType> = mock<LazyLoadingMessage<PayloadType>>().also {
    whenever(it.deserializePayload()).thenReturn(payloadValue)
  }

  private val message = DelegatingChannelMessage(
    delegate = ObjectMessage(
      headers = mapOf(
        HeaderNames.ID to "VALUE",
        HeaderNames.PAYLOAD_TYPE_CLASS_NAME to PayloadType::class.java.canonicalName,
        HeaderNames.PAYLOAD_ENCODING to "jackson"
      ), payload = payload
    ),
    payloadSupplier = { payload -> convert(payload.deserializePayload()) }
  )

  private val messageFilter = TypeListMessageFilter(setOf(PayloadType::class.java))

  private val acceptor = PersistingChannelMessageAcceptorImpl(
    messagePersistenceService = mock(),
    messageMetadataExtractorChain = MessageMetadataExtractorChain(
      HeaderMessageMetaDataSnippetExtractor(enforceMessageId = true, enforceTypeInfo = true)
    ),
    messageFilter = messageFilter,
    ingressMetrics = mock()
  )

  @Test
  fun `should not invoke payload access during metadata extraction and filtering`() {
    acceptor.accept(message)
    verifyNoInteractions(payload)
  }

  @Test
  fun `should load payload`() {
    assertThat(message.payload).isEqualTo(convert(payloadValue))
    verify(payload, times(1)).deserializePayload()
  }



  private fun convert(value: PayloadType): ByteArray = "some bytes ${value.hashCode()}".toByteArray()
}

