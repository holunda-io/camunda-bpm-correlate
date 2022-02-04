package io.holunda.camunda.bpm.correlate.event

import io.holunda.camunda.bpm.correlate.metadata.MessageMetaData
import org.springframework.stereotype.Component

@Component
class CamundaCorrelationEventFactoryRegistry(
  private val factories: List<CamundaCorrelationEventFactory>
) {

  fun getFactory(metaData: MessageMetaData): CamundaCorrelationEventFactory? {
    return factories.find { factory -> factory.supports(metaData) }
  }
}
