package io.holunda.camunda.bpm.correlate.event

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData

/**
 * This factory is responsible for mapping incoming messages to BPMN Signals or BPMN Messages.
 */
interface CamundaCorrelationEventFactory {

  /**
   * Create a camunda correlation event from message.
   * @param messageMetadata message meta data.
   * @param payload message payload.
   * @return Camunda correlation event, may be null.
   */
  fun create(messageMetadata: MessageMetaData, payload: Any): CamundaCorrelationEvent?

  /**
   * Answers the question if the message with this metadata is supported.
   * @return true, if supported, false otherwise.
   */
  fun supports(metaData: MessageMetaData): Boolean
}
