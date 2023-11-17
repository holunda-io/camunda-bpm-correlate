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
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.ProcessEngineServices
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

  /**
   * Object mapper.
   */
  @Bean
  @ConditionalOnMissingBean(name = [CORRELATE_OBJECT_MAPPER])
  @Qualifier(CORRELATE_OBJECT_MAPPER)
  fun correlateObjectMapper(): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)


  /**
   * Payload decoder / encoder.
   */
  @Bean
  @ConditionalOnMissingBean
  fun jacksonPayloadDecoder(
    @Qualifier(CORRELATE_OBJECT_MAPPER)
    objectMapper: ObjectMapper
  ): PayloadDecoder = JacksonJsonDecoder(
    objectMapper = objectMapper
  )

  /**
   * Configures mybatis mapper for messages.
   */
  @Autowired
  fun registerMyBatisMappers(processEngine: ProcessEngine) {
    val processEngineConfiguration = processEngine.processEngineConfiguration as ProcessEngineConfigurationImpl
    processEngineConfiguration.sqlSessionFactory.configuration.mapperRegistry.let { registry ->
      if (!registry.hasMapper(MyBatisMessageMapper::class.java)) {
        registry.addMapper(MyBatisMessageMapper::class.java)
      }
    }
  }

  /**
   * Message repository using MyBatis.
   */
  @ConditionalOnMissingBean
  @Bean
  fun messageRepository(processEngine: ProcessEngine): MessageRepository {
    val processEngineConfiguration = processEngine.processEngineConfiguration as ProcessEngineConfigurationImpl
    return MyBatisMessageRepository(sqlSessionFactory = processEngineConfiguration.sqlSessionFactory)
  }



  /**
   * Error handling strategy for single message error.
   */
  @ConditionalOnMissingBean
  @Bean
  fun singleMessageErrorHandlingStrategy(
    retryingErrorHandlingProperties: RetryingErrorHandlingProperties,
    clock: Clock
  ): SingleMessageErrorHandlingStrategy = RetryingSingleMessageErrorHandlingStrategy(
    retryErrorHandlingConfig = retryingErrorHandlingProperties,
    clock = clock
  )

  /**
   * Message persistence storage.
   */
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

  /**
   * Message cleanup service.
   */
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
