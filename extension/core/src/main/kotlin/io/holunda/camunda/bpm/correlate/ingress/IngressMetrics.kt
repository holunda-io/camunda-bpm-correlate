package io.holunda.camunda.bpm.correlate.ingress

import io.holunda.camunda.bpm.correlate.util.ComponentLike
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag

/**
 * Metrics for ingress.
 */
@ComponentLike
class IngressMetrics(
  private val registry: MeterRegistry
) {

  companion object {
    const val PREFIX_INGRESS = "camunda.bpm.correlate.ingress"
    const val PREFIX_ACCEPTOR = "camunda.bpm.correlate.acceptor"

    const val COUNTER_RECEIVED = "$PREFIX_INGRESS.received"
    const val COUNTER_ACCEPTED = "$PREFIX_INGRESS.accepted"
    const val COUNTER_IGNORED = "$PREFIX_INGRESS.ignored"

    const val COUNTER_PERSISTED = "$PREFIX_ACCEPTOR.persisted"
    const val COUNTER_DROPPED = "$PREFIX_ACCEPTOR.dropped"

    const val TAG_CHANNEL = "channel"
  }

  /**
   * Received by the ingress adapter.
   */
  fun incrementReceived(channel: String) {
    registry.counter(COUNTER_RECEIVED, listOf(Tag.of(TAG_CHANNEL, channel))).increment()
  }

  /**
   * Accepted by acceptor.
   */
  fun incrementAccepted(channel: String) {
    registry.counter(COUNTER_ACCEPTED, listOf(Tag.of(TAG_CHANNEL, channel))).increment()
  }

  /**
   * Ignored by acceptor.
   */
  fun incrementIgnored(channel: String) {
    registry.counter(COUNTER_IGNORED, listOf(Tag.of(TAG_CHANNEL, channel))).increment()
  }

  /**
   * Passed through message filter and persisted in inbox.
   */
  fun incrementPersisted() {
    registry.counter(COUNTER_PERSISTED).increment()
  }

  /**
   * Ignored by message filter (instead of persisted).
   */
  fun incrementDropped() {
    registry.counter(COUNTER_DROPPED).increment()
  }
}
