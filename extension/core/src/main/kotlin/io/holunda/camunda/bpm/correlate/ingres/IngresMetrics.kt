package io.holunda.camunda.bpm.correlate.ingres

import mu.KLogging

/**
 * Metrics for ingres.
 */
class IngresMetrics(
  // TODO metrics
) {
  companion object: KLogging()

  var accepted = 0
  var ignored = 0
  var received = 0

  fun incrementAccepted() {
    accepted = accepted.inc()
    logger.info { "Accepted $accepted" }
  }

  fun incrementIgnored() {
    ignored = ignored.inc()
    logger.info { "Ignored $ignored" }
  }

  fun incrementReceived() {
    received = received.inc()
    logger.info { "Received $received" }
  }
}
