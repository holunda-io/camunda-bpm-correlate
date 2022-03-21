package io.holunda.camunda.bpm.correlate.persist

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.persist.encoding.JacksonJsonDecoder
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import io.holunda.camunda.bpm.correlate.persist.error.RetryingErrorHandlingProperties
import io.holunda.camunda.bpm.correlate.persist.error.RetryingSingleMessageErrorHandlingStrategy
import io.holunda.camunda.bpm.correlate.persist.impl.DefaultMessagePersistenceService
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import io.holunda.camunda.bpm.correlate.persist.impl.MessagePersistenceProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class MessagePersistenceConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @Qualifier("correlateObjectMapper")
  fun correlateObjectMapper(): ObjectMapper = jacksonObjectMapper()
    .findAndRegisterModules()
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  @Bean
  @ConditionalOnMissingBean
  fun payloadDecoder(
    @Qualifier("correlateObjectMapper")
    objectMapper: ObjectMapper
  ): PayloadDecoder = JacksonJsonDecoder(
    objectMapper = objectMapper
  )

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
