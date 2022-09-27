package io.holunda.camunda.bpm.correlate.ingress

import io.holunda.camunda.bpm.correlate.util.ComponentLike
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter

/**
 * Metrics for ingress.
 */
@ComponentLike
class IngressMetrics(
  registry: CollectorRegistry
) {

  private val acceptedCounter = Counter
    .build()
    .name("camunda_bpm_correlate_ingress_accepted_total")
    .labelNames("channel")
    .help("Messages accepted by the ingress adapter")
    .register(registry)

  private val receivedCounter = Counter
    .build()
    .name("camunda_bpm_correlate_ingress_received_total")
    .labelNames("channel")
    .help("Messages received by the ingress adapter")
    .register(registry)

  private val ignoredCounter = Counter
    .build()
    .name("camunda_bpm_correlate_ingress_ignored_total")
    .labelNames("channel")
    .help("Messages ignored by the ingress adapter")
    .register(registry)

  private val persistedCounter = Counter
    .build()
    .name("camunda_bpm_correlate_acceptor_persisted_total")
    .help("Messages persisted by the acceptor")
    .register(registry)

  private val droppedCounter = Counter
    .build()
    .name("camunda_bpm_correlate_acceptor_dropped_total")
    .help("Messages dropped by the acceptor")
    .register(registry)

  /**
   * Received by the ingress adapter.
   */
  fun incrementReceived(channel: String) {
    receivedCounter.labels(channel).inc()
  }

  /**
   * Accepted by acceptor.
   */
  fun incrementAccepted(channel: String) {
    acceptedCounter.labels(channel).inc()
  }

  /**
   * Ignored by acceptor.
   */
  fun incrementIgnored(channel: String) {
    ignoredCounter.labels(channel).inc()
  }

  /**
   * Passed through message filter and persisted in inbox.
   */
  fun incrementPersisted() {
    persistedCounter.inc()
  }

  /**
   * Ignored by message filter (instead of persisted).
   */
  fun incrementDropped() {
    droppedCounter.inc()
  }
}
