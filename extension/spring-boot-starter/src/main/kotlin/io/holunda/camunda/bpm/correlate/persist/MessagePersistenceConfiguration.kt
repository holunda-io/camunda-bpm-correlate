package io.holunda.camunda.bpm.correlate.persist

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.holunda.camunda.bpm.correlate.CamundaBpmCorrelateConfiguration
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.persist.encoding.JacksonJsonDecoder
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import io.holunda.camunda.bpm.correlate.persist.error.RetryingErrorHandlingProperties
import io.holunda.camunda.bpm.correlate.persist.error.RetryingSingleMessageErrorHandlingStrategy
import io.holunda.camunda.bpm.correlate.persist.impl.*
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

/**
 * Configuration of message serialization and persistence.
 */
@Configuration
@ConditionalOnBean(name = ["messagePersistenceProperties"])
@AutoConfigureAfter(CamundaBpmCorrelateConfiguration::class)
class MessagePersistenceConfiguration {

  companion object {
    const val CORRELATE_OBJECT_MAPPER = "correlateObjectMapper"
  }

  @Bean
  @ConditionalOnMissingBean(name = [CORRELATE_OBJECT_MAPPER])
  @Qualifier(CORRELATE_OBJECT_MAPPER)
  fun correlateObjectMapper(): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)


  @Bean
  @ConditionalOnMissingBean
  fun jacksonPayloadDecoder(
    @Qualifier(CORRELATE_OBJECT_MAPPER)
    objectMapper: ObjectMapper
  ): PayloadDecoder = JacksonJsonDecoder(
    objectMapper = objectMapper
  )

  @Autowired
  fun registerMyBatisMappers(processEngineConfiguration: ProcessEngineConfigurationImpl) {
    processEngineConfiguration.sqlSessionFactory.configuration.mapperRegistry.let { registry ->
      if (!registry.hasMapper(MyBatisMessageMapper::class.java)) {
        registry.addMapper(MyBatisMessageMapper::class.java)
      }
    }
  }

  @ConditionalOnMissingBean
  @Bean
  fun messageRepository(processEngineConfiguration: ProcessEngineConfigurationImpl): MessageRepository =
    MyBatisMessageRepository(sqlSessionFactory = processEngineConfiguration.sqlSessionFactory)


  @ConditionalOnMissingBean
  @Bean
  fun singleMessageErrorHandlingStrategy(
    retryingErrorHandlingProperties: RetryingErrorHandlingProperties,
    clock: Clock
  ): SingleMessageErrorHandlingStrategy = RetryingSingleMessageErrorHandlingStrategy(
    retryErrorHandlingConfig = retryingErrorHandlingProperties,
    clock = clock
  )

  @ConditionalOnMissingBean
  @Bean
  fun messagePersistenceService(
    messageRepository: MessageRepository,
    messagePersistenceProperties: MessagePersistenceProperties,
    payloadDecoders: List<PayloadDecoder>,
    clock: Clock,
    singleMessageCorrelationStrategy: SingleMessageCorrelationStrategy,
    singleMessageErrorHandlingStrategy: SingleMessageErrorHandlingStrategy
  ): MessagePersistenceService =
    DefaultMessagePersistenceService(
      messagePersistenceConfig = messagePersistenceProperties,
      messageRepository = messageRepository,
      payloadDecoders = payloadDecoders,
      singleMessageCorrelationStrategy = singleMessageCorrelationStrategy,
      singleMessageErrorHandlingStrategy = singleMessageErrorHandlingStrategy,
      clock = clock,
    )

  @ConditionalOnMissingBean
  @Bean
  fun messageCleanupService(
    messageRepository: MessageRepository,
    messagePersistenceProperties: MessagePersistenceProperties,
    clock: Clock,
  ): MessageManagementService =
    MessageManagementService(
      messageRepository = messageRepository,
      persistenceConfig = messagePersistenceProperties,
      clock = clock
    )
}
