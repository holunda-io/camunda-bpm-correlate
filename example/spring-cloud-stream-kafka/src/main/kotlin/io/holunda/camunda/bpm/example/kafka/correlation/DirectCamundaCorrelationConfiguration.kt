package io.holunda.camunda.bpm.example.kafka.correlation

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.example.kafka.correlation.DirectCamundaCorrelationConfiguration.Companion.PROFILE
import io.holunda.camunda.bpm.example.kafka.correlation.direct.DirectIngressMessageConsumer
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.spring.boot.starter.configuration.CamundaProcessEngineConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile(PROFILE)
class DirectCamundaCorrelationConfiguration {

  companion object {
    const val PROFILE = "direct"
  }

  @Bean
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
