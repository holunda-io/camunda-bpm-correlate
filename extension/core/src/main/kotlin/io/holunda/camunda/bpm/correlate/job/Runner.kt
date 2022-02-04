package io.holunda.camunda.bpm.correlate.job

import io.holunda.camunda.bpm.correlate.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import io.holunda.camunda.bpm.correlate.process.CorrelationService
import org.springframework.stereotype.Component

@Component
class Runner(
  private val persistenceService: MessagePersistenceService,
  private val correlationStrategy: CorrelationStrategy,
  private val correlationService: CorrelationService
) {

  companion object {
    // FIXME -> properties
    const val fetchSize: Int = 100
  }

  fun schedule() {
    val messages: List<Pair<MessageMetaData, Any>> = persistenceService.fetchMessages(fetchSize)

    messages
      .groupBy { correlationStrategy.correlationSelector() }
      .entries
      .map { (_, messages) -> correlationService.correlateBatch(messages) }
  }

}
