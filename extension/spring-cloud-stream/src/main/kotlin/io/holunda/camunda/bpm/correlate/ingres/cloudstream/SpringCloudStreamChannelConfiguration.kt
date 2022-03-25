package io.holunda.camunda.bpm.correlate.ingres.cloudstream

import io.holunda.camunda.bpm.correlate.EnableCamundaBpmCorrelate
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfigMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
@EnableCamundaBpmCorrelate
@ConditionalOnProperty(value = ["correlate.channels.stream.channelEnabled"], havingValue = "true", matchIfMissing = false)
class SpringCloudStreamChannelConfiguration {

  companion object {
    const val TYPE = "stream"
  }

  @ConditionalOnMissingBean
  @Bean
  fun streamByteMessageConsumer(
    channelMessageAcceptor: ChannelMessageAcceptor,
    metrics: IngresMetrics,
    channelMessageHeaderConverter: ChannelMessageHeaderExtractor
  ) = StreamByteMessageConsumer(
    messageAcceptor = channelMessageAcceptor,
    metrics = metrics,
    channelMessageHeaderConverter = channelMessageHeaderConverter
  )

  @ConditionalOnMissingBean
  @Bean
  fun channelMessageHeaderConverter(): ChannelMessageHeaderExtractor = DefaultKafkaMessageHeaderConverter()

  @Bean
  @Order(10)
  fun streamChannelConfigMessageMetaDataSnippetExtractor(channelConfigs: Map<String, ChannelConfig>): MessageMetaDataSnippetExtractor =
    ChannelConfigMessageMetaDataSnippetExtractor(
      channelConfig = requireNotNull(channelConfigs[TYPE]) { "Configuration for channel '${TYPE}' is required." }
    )
}
