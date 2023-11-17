package io.holunda.camunda.bpm.example.axon.correlate

import io.holunda.camunda.bpm.correlate.CorrelateConfigurationProperties
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import io.holunda.camunda.bpm.example.axon.correlate.rest.AdminRestController
import mu.KLogging
import org.camunda.bpm.engine.RepositoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import jakarta.annotation.PostConstruct

/**
 * Configuration for correlation.
 */
@Configuration
class UsingCamundaCorrelateConfiguration(
  val correlateConfigurationProperties: CorrelateConfigurationProperties
) {
  companion object : KLogging()

  /**
   * Post some info.
   */
  @PostConstruct
  fun info() {
    logger.info { "[Camunda CORRELATE] Channels configured: ${correlateConfigurationProperties.channels.keys.joinToString(", ")}" }
    logger.info { "[Camunda CORRELATE] Axon message TTL: ${correlateConfigurationProperties.message.timeToLiveAsString ?: "none"}" }
  }

  /**
   * Event factory for reservation process.
   */
  @Bean
  fun reservationProcessingEventFactory(singleMessageCorrelationStrategy: SingleMessageCorrelationStrategy): ReservationProcessingEventFactory {
    return ReservationProcessingEventFactory(
      singleMessageCorrelationStrategy = singleMessageCorrelationStrategy
    )
  }

  /**
   * Correlation service.
   */
  @Bean
  fun reservationProcessingCorrelation(repositoryService: RepositoryService): SingleMessageCorrelationStrategy {
    return ReservationProcessingCorrelation(
      repositoryService = repositoryService
    )
  }

  /**
   * Controller for admin functionality.
   */
  @Bean
  fun adminRestController(messageManagementService: MessageManagementService) = AdminRestController(messageManagementService = messageManagementService)
}
