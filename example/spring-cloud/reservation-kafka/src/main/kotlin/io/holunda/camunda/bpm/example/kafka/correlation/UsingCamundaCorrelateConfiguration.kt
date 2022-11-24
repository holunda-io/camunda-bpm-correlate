package io.holunda.camunda.bpm.example.kafka.correlation

import io.holunda.camunda.bpm.correlate.CorrelateConfigurationProperties
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import io.holunda.camunda.bpm.example.kafka.correlation.UsingCamundaCorrelateConfiguration.Companion.PROFILE
import io.holunda.camunda.bpm.example.kafka.correlation.correlate.ReservationProcessingCorrelation
import io.holunda.camunda.bpm.example.kafka.correlation.correlate.ReservationProcessingEventFactory
import io.holunda.camunda.bpm.example.kafka.correlation.correlate.rest.AdminRestController
import mu.KLogging
import org.camunda.bpm.engine.RepositoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct

/**
 * Configuration to use the library for correlation.
 */
@Configuration
@Profile(PROFILE)
class UsingCamundaCorrelateConfiguration(
  val correlateConfigurationProperties: CorrelateConfigurationProperties
) {

  companion object : KLogging() {
    const val PROFILE = "camunda-correlate"
  }

  /**
   * Event factory.
   */
  @Bean
  fun reservationProcessingEventFactory(singleMessageCorrelationStrategy: SingleMessageCorrelationStrategy): ReservationProcessingEventFactory {
    return ReservationProcessingEventFactory(
      singleMessageCorrelationStrategy = singleMessageCorrelationStrategy
    )
  }

  /**
   * Correlation config.
   */
  @Bean
  fun reservationProcessingCorrelation(repositoryService: RepositoryService): SingleMessageCorrelationStrategy {
    return ReservationProcessingCorrelation(
      repositoryService = repositoryService
    )
  }

  /**
   * Admin controller.
   */
  @Bean
  fun adminRestController(messageManagementService: MessageManagementService) = AdminRestController(messageManagementService = messageManagementService)
}
