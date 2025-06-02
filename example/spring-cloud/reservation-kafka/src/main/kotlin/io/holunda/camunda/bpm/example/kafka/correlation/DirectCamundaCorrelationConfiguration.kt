package io.holunda.camunda.bpm.example.kafka.correlation

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.example.kafka.correlation.DirectCamundaCorrelationConfiguration.Companion.PROFILE
import io.holunda.camunda.bpm.example.kafka.correlation.direct.DirectIngressMessageConsumer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

private val logger = KotlinLogging.logger {}

/**
 * Configuration which uses näive correlation in consumer thread of the Kafka receiver.
 */
@Configuration
@Profile(PROFILE)
class DirectCamundaCorrelationConfiguration {

  companion object {
    const val PROFILE = "direct"
  }

  init {
    logger.info { "Using message consumer delivering messages directly" }
  }

  /**
   * Creates direct consumer.
   */
  @Bean("directIngressMessageConsumer")
  fun directIngressMessageConsumer(
    objectMapper: ObjectMapper,
    runtimeService: RuntimeService,
    repositoryService: RepositoryService
  ) = DirectIngressMessageConsumer(
    objectMapper = objectMapper,
    runtimeService = runtimeService,
    repositoryService = repositoryService
  )
}
