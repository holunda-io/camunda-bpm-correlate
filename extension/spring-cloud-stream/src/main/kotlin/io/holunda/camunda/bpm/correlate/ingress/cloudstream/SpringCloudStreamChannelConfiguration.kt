package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfigMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptorConfiguration
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
@ConditionalOnProperty(
  prefix = "correlate.channels.stream",
  name = ["channelEnabled"],
  matchIfMissing = false,
  havingValue = "true"
)
@AutoConfigureAfter(ChannelMessageAcceptorConfiguration::class)
class SpringCloudStreamChannelConfiguration {

  companion object {
    const val TYPE = "stream"
  }

  @ConditionalOnMissingBean
  @Bean
  fun streamByteMessageConsumer(
    channelMessageAcceptor: ChannelMessageAcceptor,
    metrics: IngressMetrics,
    channelMessageHeaderConverter: ChannelMessageHeaderConverter
  ) = StreamByteMessageConsumer(
    messageAcceptor = channelMessageAcceptor,
    metrics = metrics,
    channelMessageHeaderConverter = channelMessageHeaderConverter,
    channel = TYPE // currently based on type, later based on name
  )

  @ConditionalOnMissingBean
  @Bean
  fun channelMessageHeaderConverter(): ChannelMessageHeaderConverter = DefaultKafkaMessageHeaderConverter()

  @Bean
  @Order(10)
  fun streamChannelConfigMessageMetaDataSnippetExtractor(channelConfigs: Map<String, ChannelConfig>): MessageMetaDataSnippetExtractor =
    ChannelConfigMessageMetaDataSnippetExtractor(
      channelConfig = requireNotNull(channelConfigs[TYPE]) { "Configuration for channel '${TYPE}' is required." }
    )
}
