package io.holunda.camunda.bpm.correlate.ingress

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.MessageMetadataExtractorChain
import io.holunda.camunda.bpm.correlate.ingress.filter.AllMessageFilter
import io.holunda.camunda.bpm.correlate.ingress.impl.PersistingChannelMessageAcceptorImpl
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceConfiguration
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnBean(name = ["messageMetadataExtractorChain"])
@AutoConfigureAfter(MessagePersistenceConfiguration::class)
class ChannelMessageAcceptorConfiguration {

  @ConditionalOnMissingBean
  @Bean
  fun persistingChannelMessageAcceptor(
    messageMetadataExtractorChain: MessageMetadataExtractorChain,
    messagePersistenceService: MessagePersistenceService,
    messageFilter: MessageFilter,
    ingressMetrics: IngressMetrics
  ): ChannelMessageAcceptor = PersistingChannelMessageAcceptorImpl(
    messageMetadataExtractorChain = messageMetadataExtractorChain,
    messagePersistenceService = messagePersistenceService,
    messageFilter = messageFilter,
    ingressMetrics = ingressMetrics
  )

  @ConditionalOnMissingBean
  @Bean
  fun messageFilter(): MessageFilter = AllMessageFilter()

}
