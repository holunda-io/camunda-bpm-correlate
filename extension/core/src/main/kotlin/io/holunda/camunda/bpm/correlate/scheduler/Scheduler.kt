package io.holunda.camunda.bpm.correlate.scheduler

import io.holunda.camunda.bpm.correlate.correlation.StoreAndForwardCorrelationService

class Scheduler(
  private val defaultCorrelationService: StoreAndForwardCorrelationService
) {

  fun schedule() {
    defaultCorrelationService.correlate()
  }

}
