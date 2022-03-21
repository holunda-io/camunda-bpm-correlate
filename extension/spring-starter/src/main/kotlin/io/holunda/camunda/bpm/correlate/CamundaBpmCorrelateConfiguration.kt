package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.*
import io.holunda.camunda.bpm.correlate.correlation.impl.CamundaBpmBatchCorrelationService
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactory
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactoryRegistry
import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.ingres.impl.PersistingChannelMessageAcceptorImpl
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import io.holunda.camunda.bpm.correlate.persist.error.RetryingErrorHandlingProperties
import io.holunda.camunda.bpm.correlate.persist.impl.MessagePersistenceProperties
import io.holunda.camunda.bpm.correlate.persist.impl.MyBatisMessageMapper
import io.holunda.camunda.bpm.correlate.persist.impl.MyBatisMessageRepository
import mu.KLogging
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import java.time.Clock

@Configuration
@EnableConfigurationProperties(CorrelateConfigurationProperties::class)
@Import(BatchCorrelationSchedulerConfiguration::class)
class CamundaBpmCorrelateConfiguration {

  companion object : KLogging()

  @ConditionalOnMissingBean
  @Bean
  fun clock(): Clock = Clock.systemUTC()

  @ConditionalOnMissingBean
  @Bean
  fun ingresMetrics() = IngresMetrics()

  @ConditionalOnMissingBean
  @Bean
  fun correlationMetrics(): CorrelationMetrics = CorrelationMetrics()

  @ConditionalOnMissingBean
  @Bean
  fun channelMessageAcceptor(
    messagePersistenceService: MessagePersistenceService,
    messageMetadataExtractorChain: MessageMetadataExtractorChain
  ): ChannelMessageAcceptor = PersistingChannelMessageAcceptorImpl(
    messagePersistenceService = messagePersistenceService,
    messageMetadataExtractorChain = messageMetadataExtractorChain
  )

  @ConditionalOnMissingBean
  @Bean
  fun messageMetadataExtractorChain(extractors: List<MessageMetaDataSnippetExtractor>): MessageMetadataExtractorChain =
    MessageMetadataExtractorChain(extractors = extractors)

  @ConditionalOnMissingBean
  @Bean
  @Order(20)
  fun headerMessageMetaDataSnippetExtractor() = HeaderMessageMetaDataSnippetExtractor()

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
  fun camundaCorrelationEventFactoryRegistry(factories: List<CamundaCorrelationEventFactory>): CamundaCorrelationEventFactoryRegistry =
    CamundaCorrelationEventFactoryRegistry(factories)

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


  @Bean
  fun channelConfigs(correlateConfigurationProperties: CorrelateConfigurationProperties): Map<String, ChannelConfig> =
    correlateConfigurationProperties.channels

  @Bean
  fun messagePersistenceProperties(correlateConfigurationProperties: CorrelateConfigurationProperties): MessagePersistenceProperties =
    correlateConfigurationProperties.persistence

  @Bean
  fun retryingErrorHandlingProperties(correlateConfigurationProperties: CorrelateConfigurationProperties): RetryingErrorHandlingProperties =
    correlateConfigurationProperties.retry

  @Bean
  fun batchConfigurationProperties(correlateConfigurationProperties: CorrelateConfigurationProperties): BatchConfigurationProperties =
    correlateConfigurationProperties.batch

  @Autowired
  fun registerMyBatisMappers(processEngineConfiguration: ProcessEngineConfigurationImpl) {
    processEngineConfiguration.sqlSessionFactory.configuration.mapperRegistry.addMapper(MyBatisMessageMapper::class.java)
  }

  @ConditionalOnMissingBean
  @Bean
  fun messageRepository(processEngineConfiguration: ProcessEngineConfigurationImpl): MessageRepository =
    MyBatisMessageRepository(sqlSessionFactory = processEngineConfiguration.sqlSessionFactory)

}
