package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.correlationMessage
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import io.holunda.camunda.bpm.correlate.runningInstanceHint
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

internal class BatchCorrelationProcessorTest {

  private val batches = arrayOf(
    CorrelationBatch(
      groupingKey = runningInstanceHint("instance1").groupingKey,
      correlationMessages = listOf(correlationMessage(), correlationMessage(), correlationMessage())
    ),
    CorrelationBatch(
      groupingKey = runningInstanceHint("instance2").groupingKey,
      correlationMessages = listOf(correlationMessage(), correlationMessage())
    )
  )
  private val correlationMetrics: CorrelationMetrics = mock()
  private val persistenceService: MessagePersistenceService = mock<MessagePersistenceService>().apply {
    whenever(this.fetchMessageBatches()).thenReturn(listOf(batches[0], batches[1]))
  }
  private val batchCorrelationService: BatchCorrelationService = mock()

  private val processor = BatchCorrelationProcessor(
    persistenceService = persistenceService,
    correlationService = batchCorrelationService,
    correlationMetrics = correlationMetrics
  )

  @Test
  fun `should successfully correlate both batches`() {
    whenever(batchCorrelationService.correlateBatch(any())).thenAnswer { invocation ->
      CorrelationBatchResult.Success(successfulCorrelations = (invocation.arguments[0] as CorrelationBatch).correlationMessages.map { it.messageMetaData })
    }
    processor.correlate()

    verify(persistenceService).fetchMessageBatches()
    verify(persistenceService).success(batches[0].correlationMessages.map { it.messageMetaData })
    verify(persistenceService).success(batches[1].correlationMessages.map { it.messageMetaData })
    verifyNoMoreInteractions(persistenceService)

    verify(correlationMetrics).incrementSuccess(batches[0].correlationMessages.size)
    verify(correlationMetrics).incrementSuccess(batches[1].correlationMessages.size)
    verifyNoMoreInteractions(correlationMetrics)
  }

  @Test
  fun `should successfully first batch and stop on the second`() {
    whenever(batchCorrelationService.correlateBatch(any())).thenAnswer { invocation ->
      val batch = (invocation.arguments[0] as CorrelationBatch)
      if (batch == batches[0]) {
        CorrelationBatchResult.Success(successfulCorrelations = batch.correlationMessages.map { it.messageMetaData })
      } else {
        CorrelationBatchResult.Error(
          successfulCorrelations = batch.correlationMessages.take(1).map { it.messageMetaData },
          errorCorrelations = batch.correlationMessages.subList(1, batch.correlationMessages.size).associate { it.messageMetaData to "ERROR" },
        )
      }
    }
    processor.correlate()

    verify(persistenceService).fetchMessageBatches()
    verify(persistenceService).success(batches[0].correlationMessages.map { it.messageMetaData })
    verify(persistenceService).success(batches[1].correlationMessages.take(1).map { it.messageMetaData })
    verify(persistenceService).error(batches[1].correlationMessages.subList(1, batches[1].correlationMessages.size).associate { it.messageMetaData to "ERROR" })
    verifyNoMoreInteractions(persistenceService)

    verify(correlationMetrics).incrementSuccess(batches[0].correlationMessages.size)
    verify(correlationMetrics).incrementSuccess(1)
    verify(correlationMetrics).incrementError(batches[1].correlationMessages.size - 1)
    verifyNoMoreInteractions(correlationMetrics)

  }

  @Test
  fun `should successfully first batch and stop on the second because of exception`() {
    whenever(batchCorrelationService.correlateBatch(any())).thenAnswer { invocation ->
      val batch = (invocation.arguments[0] as CorrelationBatch)
      if (batch == batches[0]) {
        CorrelationBatchResult.Success(successfulCorrelations = batch.correlationMessages.map { it.messageMetaData })
      } else {
        throw IllegalArgumentException("Illegal Batch") // this will cancel the whole batch
      }
    }
    processor.correlate()

    verify(persistenceService).fetchMessageBatches()
    verify(persistenceService).success(batches[0].correlationMessages.map { it.messageMetaData })
    verify(persistenceService).error(batches[1].correlationMessages.take(1).associate { it.messageMetaData to "Illegal Batch" })
    verifyNoMoreInteractions(persistenceService)

    verify(correlationMetrics).incrementSuccess(batches[0].correlationMessages.size)
    verify(correlationMetrics).incrementError()
    verifyNoMoreInteractions(correlationMetrics)

  }

}
