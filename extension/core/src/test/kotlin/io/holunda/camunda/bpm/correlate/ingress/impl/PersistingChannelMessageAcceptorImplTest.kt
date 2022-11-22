package io.holunda.camunda.bpm.correlate.ingress.impl

import io.holunda.camunda.bpm.correlate.PayloadType
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.emptyMessageMetadata
import io.holunda.camunda.bpm.correlate.ingress.MessageFilter
import io.holunda.camunda.bpm.correlate.ingress.message.ByteMessage
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

internal class PersistingChannelMessageAcceptorImplTest {

  private val messageFilterMock: MessageFilter = mock()
  private val persistenceServiceMock: MessagePersistenceService = mock()
  private val messageMetadataExtractorChainMock: MessageMetadataExtractorChain = mock()


  private val acceptor = PersistingChannelMessageAcceptorImpl(
    messageMetadataExtractorChain = messageMetadataExtractorChainMock,
    messagePersistenceService = persistenceServiceMock,
    messageFilter = messageFilterMock,
    ingressMetrics = mock()
  )

  @Test
  fun `should delegate support decision to extractor chain`() {

    val headers = mapOf("Some" to "other")

    whenever(messageMetadataExtractorChainMock.supports(any())).thenReturn(true)
    assertThat(acceptor.supports(headers)).isTrue
    verify(messageMetadataExtractorChainMock).supports(headers)
    verifyNoInteractions(persistenceServiceMock)
    verifyNoInteractions(messageFilterMock)

  }

  @Test
  fun `should delegate filter decision to message filter`() {

    val headers = mapOf("Some" to "other")
    val payload = "some".toByteArray()
    val message = ByteMessage(headers, payload)
    val metadata = emptyMessageMetadata().copy(payloadTypeInfo = TypeInfo.fromClass(PayloadType::class.java))

    whenever(messageMetadataExtractorChainMock.extractChainedMetaData<ByteArray>(any())).thenReturn(metadata)
    whenever(messageFilterMock.accepts<ByteArray>(any(), any())).thenReturn(true)

    acceptor.accept(message)

    verify(messageMetadataExtractorChainMock).extractChainedMetaData(message)
    verify(messageFilterMock).accepts(message, metadata)
    verify(persistenceServiceMock).persistMessage(metadata, message)
    verifyNoMoreInteractions(persistenceServiceMock)
  }

  @Test
  fun `should not accept the message if rejected by the message filter`() {

    val headers = mapOf("Some" to "other")
    val payload = "some".toByteArray()
    val message = ByteMessage(headers, payload)
    val metadata = emptyMessageMetadata().copy(payloadTypeInfo = TypeInfo.fromClass(PayloadType::class.java))

    whenever(messageMetadataExtractorChainMock.extractChainedMetaData<ByteArray>(any())).thenReturn(metadata)
    whenever(messageFilterMock.accepts<ByteArray>(any(), any())).thenReturn(false)

    acceptor.accept(message)

    verify(messageMetadataExtractorChainMock).extractChainedMetaData(message)
    verify(messageFilterMock).accepts(message, metadata)
    verifyNoMoreInteractions(persistenceServiceMock)
  }

}