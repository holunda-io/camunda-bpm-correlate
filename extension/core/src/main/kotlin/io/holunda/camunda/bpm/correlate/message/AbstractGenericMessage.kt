package io.holunda.camunda.bpm.correlate.message

/**
 * Base class for implementation of messages.
 */
abstract class AbstractGenericMessage<P>(
  val headers: Map<String, Any>,
  val payload: P
) {
  /**
   * Retrieves the type of payload.
   */
  fun getPayloadEncoding(): Class<out P> = payload!!::class.java
}
