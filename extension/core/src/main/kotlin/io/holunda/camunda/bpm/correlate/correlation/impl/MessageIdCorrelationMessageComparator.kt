package io.holunda.camunda.bpm.correlate.correlation.impl

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage

/**
 * Correlation message comparator taking message id as a reference.
 */
class MessageIdCorrelationMessageComparator : Comparator<CorrelationMessage> {
  override fun compare(left: CorrelationMessage, right: CorrelationMessage): Int =
    left.messageMetaData.messageId.compareTo(right.messageMetaData.messageId)
}
