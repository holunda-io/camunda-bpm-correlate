package io.holunda.camunda.bpm.correlate.correlation.impl

import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder

/**
 * Used to wrap the creation of message and signal correlation in new transactions.
 */
open class TransactionalRuntimeServiceWrapper(
  private val runtimeService: RuntimeService
) : RuntimeService by runtimeService {

  override fun createMessageCorrelation(messageName: String): MessageCorrelationBuilder {
    return runtimeService.createMessageCorrelation(messageName)
  }

  override fun createSignalEvent(signalName: String): SignalEventReceivedBuilder {
    return runtimeService.createSignalEvent(signalName)
  }
}
