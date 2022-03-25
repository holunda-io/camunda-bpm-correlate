package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.correlation.impl.CamundaBpmBatchCorrelationService
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactoryRegistry
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceConfiguration
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnBean(name = ["batchConfigurationProperties"])
@AutoConfigureAfter(MessagePersistenceConfiguration::class)
class BatchCorrelationConfiguration {

  @ConditionalOnMissingBean
  @Bean
  fun batchCorrelationService(
    runtimeService: RuntimeService,
    registry: CamundaCorrelationEventFactoryRegistry,
    batchConfigurationProperties: BatchConfigurationProperties
  ): BatchCorrelationService =
    CamundaBpmBatchCorrelationService(
      runtimeService = runtimeService,
      registry = registry,
      batchCorrelationMode = batchConfigurationProperties.mode
    )

  @Bean
  fun batchCorrelationProcessor(
    persistenceService: MessagePersistenceService,
    batchCorrelationService: BatchCorrelationService,
    correlationMetrics: CorrelationMetrics
  ): BatchCorrelationProcessor =
    BatchCorrelationProcessor(
      persistenceService = persistenceService,
      correlationService = batchCorrelationService,
      correlationMetrics = correlationMetrics
    )
}
