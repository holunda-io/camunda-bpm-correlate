package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.util.ComponentLike
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import mu.KLogging
import java.util.concurrent.atomic.AtomicLong

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

  private val total = AtomicLong(0L)
  private val retrying = AtomicLong(0L)
  private val error = AtomicLong(0L)
  private val maxRetriesReached = AtomicLong(0L)
  private val inProgress = AtomicLong(0L)
  private val paused = AtomicLong(0L)

  init {
    Gauge.builder(GAUGE_MESSAGES, total::get).tag("status", "total").description("").register(registry)
    Gauge.builder(GAUGE_MESSAGES, retrying::get).tag("status", "retrying").register(registry)
    Gauge.builder(GAUGE_MESSAGES, error::get).tag("status", "error").register(registry)
    Gauge.builder(GAUGE_MESSAGES, maxRetriesReached::get).tag("status", "maxRetriesReached").register(registry)
    Gauge.builder(GAUGE_MESSAGES, inProgress::get).tag("status", "inProgress").register(registry)
    Gauge.builder(GAUGE_MESSAGES, paused::get).tag("status", "paused").register(registry)
  }

  fun reportMessageCounts(countByStatus: CountByStatus) {
    if (countByStatus.total == 0L) {
      logger.debug { "No messages found, the inbox message table is clean." }
    } else {
      logger.debug { "Message counts: total: ${countByStatus.total}, retrying: ${countByStatus.retrying}, error: ${countByStatus.error}, retries exhausted: ${countByStatus.maxRetriesReached}, in progress: ${countByStatus.inProgress}, paused: ${countByStatus.paused}" }
    }

    total.set(countByStatus.total)
    retrying.set(countByStatus.retrying)
    error.set(countByStatus.error)
    maxRetriesReached.set(countByStatus.maxRetriesReached)
    inProgress.set(countByStatus.inProgress)
    paused.set(countByStatus.paused)
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
