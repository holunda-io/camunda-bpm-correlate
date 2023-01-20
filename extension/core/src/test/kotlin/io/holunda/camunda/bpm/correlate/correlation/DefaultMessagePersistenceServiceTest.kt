package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.PayloadType2
import io.holunda.camunda.bpm.correlate.correlation.impl.MessageIdCorrelationMessageComparator
import io.holunda.camunda.bpm.correlate.messageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import io.holunda.camunda.bpm.correlate.persist.SingleMessageErrorHandlingStrategy
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import io.holunda.camunda.bpm.correlate.persist.impl.DefaultMessagePersistenceService
import io.holunda.camunda.bpm.correlate.persist.impl.MessagePersistenceConfig
import io.holunda.camunda.bpm.correlate.runningInstanceHint
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class DefaultMessagePersistenceServiceTest {

  private val messagePersistenceConfig: MessagePersistenceConfig = mock()
  private val messageRepository: MessageRepository = mock()
  private val singleMessageCorrelationStrategy: SingleMessageCorrelationStrategy = mock()
  private val singleMessageErrorHandlingStrategy: SingleMessageErrorHandlingStrategy = mock()
  private val decoder: PayloadDecoder = mock()

  private val service: DefaultMessagePersistenceService = DefaultMessagePersistenceService(
    messagePersistenceConfig = messagePersistenceConfig,
    messageRepository = messageRepository,
    payloadDecoders = listOf(decoder),
    clock = Clock.system(ZoneId.of("UTC")),
    singleMessageCorrelationStrategy = singleMessageCorrelationStrategy,
    singleMessageErrorHandlingStrategy = singleMessageErrorHandlingStrategy
  )

  private val message1 = messageEntity("1").apply { inserted = Instant.now() }
  private val message2 = messageEntity("2").apply { inserted = Instant.now().plusSeconds(1) }
  private val message3 = messageEntity("3").apply { inserted = Instant.now().plusSeconds(2) }


  @BeforeEach
  fun `setup mocks`() {
    whenever(decoder.supports(any())).thenReturn(true)
    whenever(decoder.decode<PayloadType2>(any(), any())).thenReturn(PayloadType2())
    // send second message to different batch
    whenever(singleMessageCorrelationStrategy.correlationSelector()).thenReturn {
      if (it.messageMetaData.messageId == message2.id) {
        runningInstanceHint("ONE")
      } else {
        runningInstanceHint("ANOTHER")
      }
    }

    whenever(singleMessageCorrelationStrategy.correlationMessageSorter()).thenReturn(MessageIdCorrelationMessageComparator())

    whenever(messageRepository.findAll(any(), any())).thenReturn(listOf(message1, message2, message3))
    listOf(message1, message2, message3).forEach {
      whenever(messageRepository.findByIdOrNull(it.id)).thenReturn(it)
    }
  }

  @Test
  fun `should build batches`() {

    whenever(messagePersistenceConfig.batchSizeLimit()).thenReturn(-1)

    val batches = service.fetchMessageBatches()
    assertThat(batches.size).isEqualTo(2)
    assertThat(batches[0].correlationMessages.map { it.messageMetaData.messageId }).containsExactlyInAnyOrderElementsOf(listOf(message1.id, message3.id))
    assertThat(batches[1].correlationMessages.map { it.messageMetaData.messageId }).containsExactlyElementsOf(listOf(message2.id))
  }

  @Test
  fun `should build batches and take the entire batch because the limit is high`() {
    whenever(messagePersistenceConfig.batchSizeLimit()).thenReturn(5)

    val batches = service.fetchMessageBatches()
    assertThat(batches.size).isEqualTo(2)
    assertThat(batches[0].correlationMessages.map { it.messageMetaData.messageId }).containsExactlyInAnyOrderElementsOf(listOf(message1.id, message3.id))
    assertThat(batches[1].correlationMessages.map { it.messageMetaData.messageId }).containsExactlyElementsOf(listOf(message2.id))
  }


  @Test
  fun `should build batches and take only first message`() {
    whenever(messagePersistenceConfig.batchSizeLimit()).thenReturn(1)

    val batches = service.fetchMessageBatches()
    assertThat(batches.size).isEqualTo(2)
    assertThat(batches[0].correlationMessages.map { it.messageMetaData.messageId }).containsExactlyElementsOf(listOf(message1.id))
    assertThat(batches[1].correlationMessages.map { it.messageMetaData.messageId }).containsExactlyElementsOf(listOf(message2.id))
  }


  @Test
  fun `should build batches and not process them`() {
    whenever(messagePersistenceConfig.batchSizeLimit()).thenReturn(0)

    val batches = service.fetchMessageBatches()
    assertThat(batches.size).isEqualTo(2)
    assertThat(batches[0].correlationMessages).isEmpty()
    assertThat(batches[1].correlationMessages).isEmpty()
  }
}
