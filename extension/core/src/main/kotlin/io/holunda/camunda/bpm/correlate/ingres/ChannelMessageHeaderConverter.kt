package io.holunda.camunda.bpm.correlate.ingres

/**
 * Converts channel headers into message headers.
 * This class is intended to be implemented by user.
 */
interface ChannelMessageHeaderConverter {

  /**
   * Convert channel headers into generic message headers.
   * @param channelHeaders headers delivered via channel.
   * @return headers as expected by acceptor.
   */
  fun convertChannelHeaders(channelHeaders: Map<String, Any>): Map<String, Any>
}
