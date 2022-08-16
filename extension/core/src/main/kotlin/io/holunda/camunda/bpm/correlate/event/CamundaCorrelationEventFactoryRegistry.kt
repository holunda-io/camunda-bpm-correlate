package io.holunda.camunda.bpm.correlate.event

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData

/**
 * A registry for [CamundaCorrelationEventFactory] instances. There might be multiple factories available,
 * for example one per process and each factory only supports certain events, expressed by the [CamundaCorrelationEventFactory.supports] method.
 */
class CamundaCorrelationEventFactoryRegistry(
  private val factories: List<CamundaCorrelationEventFactory>
) {

  /**
   * Finds a matching factory, taking the first one, if more than one is present.
   */
  fun getFactory(metaData: MessageMetaData): CamundaCorrelationEventFactory? {
    return factories.find { factory -> factory.supports(metaData) }
  }
}
