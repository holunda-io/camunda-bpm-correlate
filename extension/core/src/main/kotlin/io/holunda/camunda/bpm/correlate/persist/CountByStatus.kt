package io.holunda.camunda.bpm.correlate.persist

/**
 * Count of messages by status.
 */
class CountByStatus(
  var total: Long,
  var maxRetriesReached: Long,
  var error: Long,
  var retrying: Long,
  var inProgress: Long,
  var paused: Long
)