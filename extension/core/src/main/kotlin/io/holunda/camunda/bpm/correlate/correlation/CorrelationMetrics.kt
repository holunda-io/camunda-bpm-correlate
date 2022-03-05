package io.holunda.camunda.bpm.correlate.correlation

import mu.KLogging

class CorrelationMetrics {

  companion object: KLogging()

  fun incrementSuccess(size: Int) {
    logger.debug { "Correlated $size messages" }
  }

  fun incrementError(size: Int) {
    logger.debug { "Error during correlation of $size messages" }
  }
}
