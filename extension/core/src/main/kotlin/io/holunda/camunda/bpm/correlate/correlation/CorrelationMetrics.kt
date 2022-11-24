package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.util.ComponentLike
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import mu.KLogging
import java.util.concurrent.atomic.AtomicLong

/**
 * Captures correlation metrics.
 */
@ComponentLike
class CorrelationMetrics(
  private val registry: MeterRegistry
) {

  companion object : KLogging() {
    const val PREFIX = "camunda.bpm.correlate"

    const val COUNTER_CORRELATED = "$PREFIX.correlation.success"
    const val COUNTER_ERROR = "$PREFIX.correlation.error"
    const val GAUGE_MESSAGES = "$PREFIX.inbox.messages"

    const val TAG_STATUS = "status"
  }

  private val total = AtomicLong(0L)
  private val retrying = AtomicLong(0L)
  private val error = AtomicLong(0L)
  private val maxRetriesReached = AtomicLong(0L)
  private val inProgress = AtomicLong(0L)
  private val paused = AtomicLong(0L)

  init {
    Gauge.builder(GAUGE_MESSAGES, total::get).tag(TAG_STATUS, "total").register(registry)
    Gauge.builder(GAUGE_MESSAGES, retrying::get).tag(TAG_STATUS, "retrying").register(registry)
    Gauge.builder(GAUGE_MESSAGES, error::get).tag(TAG_STATUS, "error").register(registry)
    Gauge.builder(GAUGE_MESSAGES, maxRetriesReached::get).tag(TAG_STATUS, "maxRetriesReached").register(registry)
    Gauge.builder(GAUGE_MESSAGES, inProgress::get).tag(TAG_STATUS, "inProgress").register(registry)
    Gauge.builder(GAUGE_MESSAGES, paused::get).tag(TAG_STATUS, "paused").register(registry)
  }

  /**
   * Reports message count.
   */
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

  /**
   * Increment correlation success counter.
   */
  fun incrementSuccess(size: Int) {
    registry.counter(COUNTER_CORRELATED).increment(size.toDouble())
  }

  /**
   * Increment correlation error counter by number of messages.
   */
  fun incrementError(size: Int) {
    registry.counter(COUNTER_ERROR).increment(size.toDouble())
  }

  /**
   * Increment correlation success counter by one.
   */
  fun incrementError() {
    incrementError(1)
  }
}
