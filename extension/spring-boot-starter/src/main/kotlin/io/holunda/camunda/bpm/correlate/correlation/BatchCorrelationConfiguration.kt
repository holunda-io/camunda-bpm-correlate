package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.correlation.impl.CamundaBpmBatchCorrelationService
import io.holunda.camunda.bpm.correlate.correlation.impl.TransactionalRuntimeServiceWrapper
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactoryRegistry
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceConfiguration
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional

@Configuration
@ConditionalOnBean(name = ["batchConfigurationProperties"])
@AutoConfigureAfter(MessagePersistenceConfiguration::class)
@EnableTransactionManagement
class BatchCorrelationConfiguration {

  companion object {
    const val TRANSACTIONAL_RUNTIME_SERVICE = "transactionalRuntimeService"
  }

  @ConditionalOnMissingBean
  @Bean
  @Transactional(propagation = REQUIRES_NEW)
  @Qualifier(TRANSACTIONAL_RUNTIME_SERVICE)
  fun transactionalRuntimeServiceWrapper(runtimeService: RuntimeService) = TransactionalRuntimeServiceWrapper(runtimeService)

  @ConditionalOnMissingBean
  @Bean
  fun batchCorrelationService(
    @Qualifier(TRANSACTIONAL_RUNTIME_SERVICE)
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
