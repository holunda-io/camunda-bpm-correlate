package io.holunda.camunda.bpm.correlate.correlation.impl

import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * Used to wrap the creation of message and signal correlation in new transactions.
 */
open class TransactionalRuntimeServiceWrapper(
  private val runtimeService: RuntimeService
) : RuntimeService by runtimeService {

  override fun createMessageCorrelation(messageName: String): MessageCorrelationBuilder {
    CamundaBpmBatchCorrelationService.logger.error { "Transaction is ${TransactionSynchronizationManager.isActualTransactionActive()}" }
    return runtimeService.createMessageCorrelation(messageName)
  }

  override fun createSignalEvent(signalName: String): SignalEventReceivedBuilder {
    CamundaBpmBatchCorrelationService.logger.error { "Transaction is ${TransactionSynchronizationManager.isActualTransactionActive()}" }
    return runtimeService.createSignalEvent(signalName)
  }
}
