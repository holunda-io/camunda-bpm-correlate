package io.holunda.camunda.bpm.correlate.persist

import io.holunda.camunda.bpm.correlate.correlation.CorrelationBatch
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData
import io.holunda.camunda.bpm.correlate.ingress.message.ChannelMessage

/**
 * Abstraction of message persistence.
 */
interface MessagePersistenceService {
  /**
   * Fetches messages.
   * @return message batches.
   */
  fun fetchMessageBatches(): List<CorrelationBatch>

  /**
   * Protocol success of correlation.
   */
  fun success(successfulCorrelations: List<MessageMetaData>)

  /**
   * Protocol error of correlations.
   */
  fun error(errorCorrelations: Map<MessageMetaData, String>)

  /**
   * Persists the received message.
   * @param metaData metadata extracted from the message.
   * @param channelMessage message to store.
   */
  fun <P, M : ChannelMessage<P>> persistMessage(metaData: MessageMetaData, channelMessage: M)

}
