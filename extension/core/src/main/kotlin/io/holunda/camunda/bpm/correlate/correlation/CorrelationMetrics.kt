package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.util.ComponentLike
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import mu.KLogging

@ComponentLike
class CorrelationMetrics(
  private val registry: MeterRegistry
) {

  companion object : KLogging() {
    const val PREFIX = "camunda.bpm.correlate"

    const val COUNTER_CORRELATED = "$PREFIX.correlation.success"
    const val COUNTER_ERROR = "$PREFIX.correlation.error"
    const val GAUGE_MESSAGES = "$PREFIX.inbox.messages"
  }

  fun reportMessageCounts(countByStatus: CountByStatus) {
    if (countByStatus.total == 0L) {
      logger.debug { "No messages found - table is clean." }
    } else {
      logger.debug { "Message counts: Total ${countByStatus.total}, eligible for processing ${countByStatus.retrying}, error ${countByStatus.error}, retries exhausted ${countByStatus.maxRetriesReached}" }
    }

    registry.gauge(GAUGE_MESSAGES, listOf(Tag.of("status", "total")), countByStatus.total.toDouble())
    registry.gauge(GAUGE_MESSAGES, listOf(Tag.of("status", "retrying")), countByStatus.retrying.toDouble())
    registry.gauge(GAUGE_MESSAGES, listOf(Tag.of("status", "error")), countByStatus.error.toDouble())
    registry.gauge(GAUGE_MESSAGES, listOf(Tag.of("status", "maxRetriesReached")), countByStatus.maxRetriesReached.toDouble())
    registry.gauge(GAUGE_MESSAGES, listOf(Tag.of("status", "paused")), countByStatus.paused.toDouble())
    registry.gauge(GAUGE_MESSAGES, listOf(Tag.of("status", "inProgress")), countByStatus.inProgress.toDouble())
  }

  fun incrementSuccess(size: Int) {
    registry.counter(COUNTER_CORRELATED).increment(size.toDouble())
  }

  fun incrementError(size: Int) {
    registry.counter(COUNTER_ERROR).increment(size.toDouble())
  }

  fun incrementError() {
    incrementError(1)
  }
}
