package io.holunda.camunda.bpm.correlate.correlation.impl

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage

/**
 * Correlation message comparator taking not changing the order.
 */
class IdentityMessageComparator : Comparator<CorrelationMessage> {
  override fun compare(left: CorrelationMessage, right: CorrelationMessage): Int = -1
}


