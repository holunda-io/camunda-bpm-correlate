package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.BatchConfigurationProperties
import io.holunda.camunda.bpm.correlate.correlation.CorrelationMetrics
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactory
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactoryRegistry
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import io.holunda.camunda.bpm.correlate.persist.error.RetryingErrorHandlingProperties
import io.holunda.camunda.bpm.correlate.persist.impl.MessagePersistenceProperties
import mu.KLogging
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.time.Clock

@Configuration
@ConditionalOnProperty(
  prefix = "correlate",
  name = ["enabled"],
  matchIfMissing = true,
  havingValue = "true"
)
@AutoConfigureAfter(CamundaBpmAutoConfiguration::class)
@EnableConfigurationProperties(CorrelateConfigurationProperties::class)
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
  fun messageMetadataExtractorChain(extractors: List<MessageMetaDataSnippetExtractor>): MessageMetadataExtractorChain =
    MessageMetadataExtractorChain(extractors = extractors)

  @ConditionalOnMissingBean
  @Bean
  @Order(20)
  fun headerMessageMetaDataSnippetExtractor() = HeaderMessageMetaDataSnippetExtractor(
    enforceMessageId = true,
    enforceTypeInfo = true
  )

  @Bean
  fun camundaCorrelationEventFactoryRegistry(factories: List<CamundaCorrelationEventFactory>): CamundaCorrelationEventFactoryRegistry =
    CamundaCorrelationEventFactoryRegistry(factories)

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

}
