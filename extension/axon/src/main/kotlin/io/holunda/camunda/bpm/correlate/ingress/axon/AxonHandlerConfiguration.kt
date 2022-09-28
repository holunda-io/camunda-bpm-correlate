package io.holunda.camunda.bpm.correlate.ingress.axon

import org.axonframework.config.EventProcessingConfigurer
import org.springframework.beans.factory.annotation.Autowired


class AxonHandlerConfiguration {
  @Autowired
  fun configureProcessingGroupErrorHandling(
    processingConfigurer: EventProcessingConfigurer,
    axonEventMessageHandler: AxonEventMessageHandler
  ) {
    processingConfigurer.registerEventHandler {
      axonEventMessageHandler
    }
  }
}
