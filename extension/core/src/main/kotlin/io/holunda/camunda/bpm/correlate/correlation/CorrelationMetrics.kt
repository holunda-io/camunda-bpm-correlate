package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.util.ComponentLike
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import io.prometheus.client.Gauge
import mu.KLogging

@ComponentLike
class CorrelationMetrics(
  registry: CollectorRegistry
) {

  companion object : KLogging()

  private val correlatedMessagesCounter: Counter = Counter.build()
    .name("camunda_bpm_correlate_correlation_success_total")
    .labelNames()
    .help("Messages successfully correlated")
    .register(registry)

  private val errorsMessagesCounter: Counter = Counter.build()
    .name("camunda_bpm_correlate_correlation_error_total")
    .labelNames()
    .help("Messages producing an error during correlation")
    .register(registry)

  private val totalMessages: Gauge = Gauge.build()
    .name("camunda_bpm_correlate_messages")
    .labelNames("status")
    .help("Handling messages currently waiting to be processed. Label: status - total, retrying, error, paused, inProgress or maxRetriesReached")
    .register(registry)


  fun reportMessageCounts(countByStatus: CountByStatus) {
    if (countByStatus.total == 0L) {
      logger.debug { "No messages found - table is clean." }
    } else {
      logger.debug { "Message counts: Total ${countByStatus.total}, eligible for processing ${countByStatus.retrying}, error ${countByStatus.error}, retries exhausted ${countByStatus.maxRetriesReached}" }
    }
    totalMessages.labels("total").set(countByStatus.total.toDouble())
    totalMessages.labels("retrying").set(countByStatus.retrying.toDouble())
    totalMessages.labels("error").set(countByStatus.error.toDouble())
    totalMessages.labels("maxRetriesReached").set(countByStatus.maxRetriesReached.toDouble())
    totalMessages.labels("paused").set(countByStatus.paused.toDouble())
    totalMessages.labels("inProgress").set(countByStatus.inProgress.toDouble())
  }

  fun incrementSuccess(size: Int) {
    correlatedMessagesCounter.inc(size.toDouble())
  }

  fun incrementError(size: Int) {
    errorsMessagesCounter.inc(size.toDouble())
  }

  fun incrementError() {
    incrementError(1)
  }


}
