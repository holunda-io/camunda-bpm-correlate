package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.BatchConfigurationProperties
import io.holunda.camunda.bpm.correlate.correlation.BatchCorrelationService
import io.holunda.camunda.bpm.correlate.correlation.CorrelationMetrics
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.GlobalConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.GlobalConfigMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactory
import io.holunda.camunda.bpm.correlate.event.CamundaCorrelationEventFactoryRegistry
import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import io.holunda.camunda.bpm.correlate.persist.error.RetryingErrorHandlingProperties
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import io.holunda.camunda.bpm.correlate.persist.impl.MessagePersistenceProperties
import io.micrometer.core.instrument.MeterRegistry
import mu.KLogging
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
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
class CamundaBpmCorrelateConfiguration : ApplicationContextAware {

  companion object : KLogging() {
    /**
     * This is a dirty hack to access application context from cockpit plugins instantiated by the SPI.
     */
    lateinit var applicationContext: ApplicationContext
  }

  @ConditionalOnMissingBean
  @Bean
  fun clock(): Clock = Clock.systemUTC()

  @ConditionalOnMissingBean
  @Bean
  fun ingressMetrics(registry: MeterRegistry) = IngressMetrics(registry = registry)

  @ConditionalOnMissingBean
  @Bean
  fun correlationMetrics(registry: MeterRegistry): CorrelationMetrics = CorrelationMetrics(registry = registry)

  @ConditionalOnMissingBean
  @Bean
  fun messageMetadataExtractorChain(extractors: List<MessageMetaDataSnippetExtractor>): MessageMetadataExtractorChain =
    MessageMetadataExtractorChain(extractors = extractors)

  @Bean
  @Qualifier("header")
  @Order(20)
  fun headerMessageMetaDataSnippetExtractor(): MessageMetaDataSnippetExtractor = HeaderMessageMetaDataSnippetExtractor(
    enforceMessageId = true,
    enforceTypeInfo = true
  )

  @Bean
  @Qualifier("global")
  @Order(10)
  fun globalConfigMessageMetaDataSnippetExtractor(globalConfig: GlobalConfig): MessageMetaDataSnippetExtractor =
    GlobalConfigMessageMetaDataSnippetExtractor(
      globalConfig = globalConfig
    )


  @Bean
  fun camundaCorrelationEventFactoryRegistry(factories: List<CamundaCorrelationEventFactory>): CamundaCorrelationEventFactoryRegistry =
    CamundaCorrelationEventFactoryRegistry(factories)

  @Bean
  fun channelConfigs(correlateConfigurationProperties: CorrelateConfigurationProperties): Map<String, ChannelConfigurationProperties> =
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

  @Bean
  fun camundaBpmCorrelateService(
    configuration: CorrelateConfigurationProperties,
    messagePersistenceService: MessagePersistenceService,
    batchCorrelationService: BatchCorrelationService,
    messageRepository: MessageRepository,
    messageManagementService: MessageManagementService
  ) = CamundaBpmCorrelateServices(
    configuration = configuration,
    messagePersistenceService = messagePersistenceService,
    batchCorrelationService = batchCorrelationService,
    messageManagementService = messageManagementService,
    messageRepository = messageRepository
  )

//  @Order(Int.MAX_VALUE)
//  @EnableWebSecurity
//  @ConditionalOnProperty("correlate.configure-csrf-behavior", havingValue = "true", matchIfMissing = false)
//  class CsrfSecurityAdapter : WebSecurityConfigurerAdapter() {
//    init {
//      logger.info { "[Camunda CORRELATE]: Using Cookie-based CSRF repository." }
//    }
//    override fun configure(http: HttpSecurity) {
//      http
//        .csrf()
//        .disable()
//      // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//    }
//  }

  override fun setApplicationContext(applicationContext: ApplicationContext) {
    CamundaBpmCorrelateConfiguration.applicationContext = applicationContext
  }

}
