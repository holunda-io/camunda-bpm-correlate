package io.holunda.camunda.bpm.correlate.event

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaData

class CamundaCorrelationEventFactoryRegistry(
  private val factories: List<CamundaCorrelationEventFactory>
) {

  fun getFactory(metaData: MessageMetaData): CamundaCorrelationEventFactory? {
    return factories.find { factory -> factory.supports(metaData) }
  }
}
