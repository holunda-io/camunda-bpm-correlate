package io.holunda.camunda.bpm.correlate.scheduler

import io.holunda.camunda.bpm.correlate.correlation.DefaultCorrelationService

class Scheduler(
  private val defaultCorrelationService: DefaultCorrelationService
) {

  fun schedule() {
    defaultCorrelationService.correlate()
  }

}
