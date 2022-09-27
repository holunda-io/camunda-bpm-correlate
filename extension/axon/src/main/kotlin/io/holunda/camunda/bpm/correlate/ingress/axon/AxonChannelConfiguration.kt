package io.holunda.camunda.bpm.correlate.ingress.axon

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfigMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptorConfiguration
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
@ConditionalOnProperty(
  prefix = "correlate.channels.axon",
  name = ["channelEnabled"],
  matchIfMissing = false,
  havingValue = "true"
)
@AutoConfigureAfter(AxonAutoConfiguration::class, ChannelMessageAcceptorConfiguration::class)
class AxonChannelConfiguration {

  companion object {
    const val TYPE = "axon"
  }

  @ConditionalOnMissingBean
  @Bean
  fun axonEventMessageHandler(
    channelMessageAcceptor: ChannelMessageAcceptor,
    metrics: IngressMetrics,
    axonEventHeaderConverter: AxonEventHeaderConverter,
    payloadDecoders: List<PayloadDecoder>,
    channelConfigs: Map<String, ChannelConfig>
  ): AxonEventMessageHandler {

    val config = requireNotNull(channelConfigs[TYPE]) { "Configuration for channel 'axon' is required." }
    val encoding = requireNotNull(config.getMessagePayloadEncoding()) { "Channel encoding is required, please set message-payload-encoding." }
    val encoder = requireNotNull(payloadDecoders.find { it.supports(encoding) }) { "Could not find decoder for configured message encoding '$encoding'." }
    return AxonEventMessageHandler(
      messageAcceptor = channelMessageAcceptor,
      metrics = metrics,
      axonEventHeaderConverter = axonEventHeaderConverter,
      encoder = encoder,
      channel = TYPE
    )
  }

  @ConditionalOnMissingBean
  @Bean
  fun axonEventHeaderExtractor(channelConfigs: Map<String, ChannelConfig>): AxonEventHeaderConverter = DefaultAxonEventHeaderConverter()

  @Bean
  @Order(10)
  fun axonChannelConfigMessageMetaDataSnippetExtractor(channelConfigs: Map<String, ChannelConfig>): MessageMetaDataSnippetExtractor =
    ChannelConfigMessageMetaDataSnippetExtractor(
      channelConfig = requireNotNull(channelConfigs[TYPE]) { "Configuration for channel 'axon' is required." }
    )
}
