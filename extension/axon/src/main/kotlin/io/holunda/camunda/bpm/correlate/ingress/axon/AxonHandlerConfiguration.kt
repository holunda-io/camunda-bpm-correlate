package io.holunda.camunda.bpm.correlate.ingress.axon

import mu.KLogging
import org.axonframework.config.EventProcessingConfigurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter

/**
 * Configuration of Axon Channel handlers to be registered to receive events from Axon Event Bus.
 */
@AutoConfiguration
@AutoConfigureAfter(AxonChannelConfiguration::class)
class AxonHandlerConfiguration {

  companion object : KLogging()

    /**
     * Configures the handlers.
     */
    @Autowired
  fun configureProcessingGroupEventHandling(
    @Autowired
    processingConfigurer: EventProcessingConfigurer,
    @Autowired(required = false)
    axonEventMessageHandlers: List<AxonEventMessageHandler>?
  ) {
    axonEventMessageHandlers?.forEach { handler ->

      logger.info { "[Camunda CORRELATE]: Configuring axon event handler for channel ${handler.channelName}." }

      processingConfigurer.registerEventHandler {
        handler
      }
    }

  }
}
