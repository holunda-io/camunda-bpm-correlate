package io.holunda.camunda.bpm.correlate.correlation.metadata.extractor

/**
 * Configuration of the channel.
 */
interface ChannelConfig {
  /**
   * Flag to enable the channel.
   */
  fun isEnabled(): Boolean
  /**
   * TTL of messages received via channel.
   */
  fun getMessageTimeToLiveAsString(): String?

  /**
   * Type of payload encoding of the message.
   */
  fun getMessagePayloadEncoding(): String?
}
