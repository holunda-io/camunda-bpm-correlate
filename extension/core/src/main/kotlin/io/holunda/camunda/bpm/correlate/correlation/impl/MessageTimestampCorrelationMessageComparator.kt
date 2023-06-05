package io.holunda.camunda.bpm.correlate.correlation.impl

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage

/**
 * Correlation message comparator taking message timestamp as a reference.
 */
class MessageTimestampCorrelationMessageComparator : Comparator<CorrelationMessage> {
  override fun compare(left: CorrelationMessage, right: CorrelationMessage): Int =
    left.messageMetaData.messageTimestamp.compareTo(right.messageMetaData.messageTimestamp)
}
