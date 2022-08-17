package io.holunda.camunda.bpm.correlate.correlation

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
class ClusterSetupProperties(
  /**
   * Activates cluster support.
   */
  val enabled: Boolean = false,
  /**
   * Lock interval for a cluster node to avoid concurrent access to the message queue. Please use the duration format, like PT5M.
   */
  val queuePollLockMostInterval: String? = null
)