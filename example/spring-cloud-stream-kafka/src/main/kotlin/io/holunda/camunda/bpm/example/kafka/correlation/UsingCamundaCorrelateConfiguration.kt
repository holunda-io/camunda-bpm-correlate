package io.holunda.camunda.bpm.example.kafka.correlation

import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import io.holunda.camunda.bpm.example.kafka.correlation.correlate.ReservationProcessingCorrelation
import io.holunda.camunda.bpm.example.kafka.correlation.correlate.ReservationProcessingEventFactory
import io.holunda.camunda.bpm.example.kafka.correlation.correlate.rest.AdminRestController
import org.camunda.bpm.engine.RepositoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UsingCamundaCorrelateConfiguration {

  companion object {
    const val PROFILE = "camunda-correlate"
  }

  @Bean
  fun reservationProcessingEventFactory(singleMessageCorrelationStrategy: SingleMessageCorrelationStrategy): ReservationProcessingEventFactory {
    return ReservationProcessingEventFactory(
      singleMessageCorrelationStrategy = singleMessageCorrelationStrategy
    )
  }

  @Bean
  fun reservationProcessingCorrelation(repositoryService: RepositoryService): SingleMessageCorrelationStrategy {
    return ReservationProcessingCorrelation(
      repositoryService = repositoryService
    )
  }

  @Bean
  fun adminRestController(messageManagementService: MessageManagementService) = AdminRestController(messageManagementService = messageManagementService)
}
