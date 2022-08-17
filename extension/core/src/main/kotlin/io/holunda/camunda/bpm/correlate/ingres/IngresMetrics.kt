package io.holunda.camunda.bpm.correlate.ingres

import io.holunda.camunda.bpm.correlate.util.ComponentLike
import mu.KLogging

/**
 * Metrics for ingres.
 */
@ComponentLike
class IngresMetrics(
  // TODO metrics
) {
  companion object : KLogging()

  var accepted = 0
  var ignored = 0
  var received = 0
  var filteredOut = 0
  var persisted = 0

  /**
   * Received by the ingres adapter.
   */
  fun incrementReceived() {
    received = received.inc()
    logger.info { "Received $received" }
  }

  /**
   * Accepted by acceptor.
   */
  fun incrementAccepted() {
    accepted = accepted.inc()
    logger.info { "Accepted $accepted" }
  }

  /**
   * Ignored by acceptor.
   */
  fun incrementIgnored() {
    ignored = ignored.inc()
    logger.info { "Ignored $ignored" }
  }

  fun incrementPersisted() {
    persisted = persisted.inc()
    logger.info { "Persisted $persisted" }
  }

  fun incrementFilteredOut() {
    filteredOut = filteredOut.inc()
    logger.info { "Filtered out by message filter $filteredOut" }
  }

}
