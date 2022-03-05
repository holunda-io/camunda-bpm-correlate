package io.holunda.camunda.bpm.correlate.ingres

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.ingres.impl.PersistingChannelMessageAcceptorImpl
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChannelMessageAcceptorConfiguration {

  @ConditionalOnMissingBean
  @Bean
  fun persistingChannelMessageAcceptor(
    messageMetadataExtractorChain: MessageMetadataExtractorChain,
    messagePersistenceService: MessagePersistenceService
  ): ChannelMessageAcceptor = PersistingChannelMessageAcceptorImpl(
    messageMetadataExtractorChain = messageMetadataExtractorChain,
    messagePersistenceService = messagePersistenceService
  )
}
