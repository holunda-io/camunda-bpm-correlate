package io.holunda.camunda.bpm.correlate.ingress

import io.holunda.camunda.bpm.correlate.util.ComponentLike
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import mu.KLogging

/**
 * Metrics for ingress.
 */
@ComponentLike
class IngressMetrics(
  registry: MeterRegistry
) {
  companion object : KLogging()

  private val acceptedCounter = Counter
    .builder("camunda_bpm_correlate_ingress_accepted")
    .description("Messages accepted by the ingress adapter")
    .register(registry)

  private val receivedCounter = Counter
    .builder("camunda_bpm_correlate_ingress_received")
    .description("Messages received by the ingress adapter")
    .register(registry)

  private val ignoredCounter = Counter
    .builder("camunda_bpm_correlate_ingress_ignored")
    .description("Messages ignored by the ingress adapter")
    .register(registry)

  private val persistedCounter = Counter
    .builder("camunda_bpm_correlate_acceptor_persisted")
    .description("Messages persisted by the acceptor")
    .register(registry)

  private val droppedCounter = Counter
    .builder("camunda_bpm_correlate_acceptor_dropped")
    .description("Messages dropped by the acceptor")
    .register(registry)


  /**
   * Received by the ingress adapter.
   */
  fun incrementReceived() {
    receivedCounter.increment()
  }

  /**
   * Accepted by acceptor.
   */
  fun incrementAccepted() {
    acceptedCounter.increment()
  }

  /**
   * Ignored by acceptor.
   */
  fun incrementIgnored() {
    ignoredCounter.increment()
  }

  /**
   * Passed through message filter and persisted in inbox.
   */
  fun incrementPersisted() {
    persistedCounter.increment()
  }

  /**
   * Ignored by message filter (instead of persisted).
   */
  fun incrementDropped() {
    droppedCounter.increment()
  }

}
