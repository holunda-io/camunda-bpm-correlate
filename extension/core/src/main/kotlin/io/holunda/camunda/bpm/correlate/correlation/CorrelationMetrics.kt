package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.util.ComponentLike
import mu.KLogging

@ComponentLike
class CorrelationMetrics {

  // FIXME: integrate metrics library
  companion object: KLogging()

  fun incrementSuccess(size: Int) {
    logger.debug { "Correlated $size messages" }
  }

  fun incrementError(size: Int) {
    logger.debug { "Error during correlation of $size messages" }
  }
}
