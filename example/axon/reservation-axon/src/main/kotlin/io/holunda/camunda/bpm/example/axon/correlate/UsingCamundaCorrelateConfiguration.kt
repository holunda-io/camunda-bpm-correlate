package io.holunda.camunda.bpm.example.axon.correlate

import io.holunda.camunda.bpm.correlate.CorrelateConfigurationProperties
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import io.holunda.camunda.bpm.example.axon.correlate.rest.AdminRestController
import mu.KLogging
import org.camunda.bpm.engine.RepositoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class UsingCamundaCorrelateConfiguration(
  val correlateConfigurationProperties: CorrelateConfigurationProperties
) {

  companion object : KLogging() {
    const val PROFILE = "camunda-correlate"
  }

  @PostConstruct
  fun info() {
    logger.info { "[Camunda CORRELATE] Channels configured: ${correlateConfigurationProperties.channels.keys.joinToString(", ")}" }
    val streamsConfig = requireNotNull(correlateConfigurationProperties.channels["axon"])
    logger.info { "[Camunda CORRELATE] Streams message TTL: ${streamsConfig.message.timeToLiveAsString ?: "none"}" }
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
