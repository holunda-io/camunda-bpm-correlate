package io.holunda.camunda.bpm.correlate.ingres.axon

import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfigMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptorConfiguration
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
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

  @ConditionalOnMissingBean
  @Bean
  fun axonEventMessageHandler(
    channelMessageAcceptor: ChannelMessageAcceptor,
    metrics: IngresMetrics,
    axonEventHeaderExtractor: AxonEventHeaderExtractor,
    payloadDecoders: List<PayloadDecoder>,
    channelConfigs: Map<String, ChannelConfig>
  ): AxonEventMessageHandler {

    val config = requireNotNull(channelConfigs["axon"]) { "Configuration for channel 'axon' is required." }
    val encoding = requireNotNull(config.getMessagePayloadEncoding()) { "Channel encoding is required, please set message-payload-encoding." }
    val encoder = requireNotNull(payloadDecoders.find { it.supports(encoding) }) { "Could not find decoder for configured message encoding '$encoding'." }
    return AxonEventMessageHandler(
      messageAcceptor = channelMessageAcceptor,
      metrics = metrics,
      axonEventHeaderExtractor = axonEventHeaderExtractor,
      encoder = encoder
    )
  }

  @ConditionalOnMissingBean
  @Bean
  fun axonEventHeaderExtractor(channelConfigs: Map<String, ChannelConfig>): AxonEventHeaderExtractor =
    DefaultAxonEventHeaderExtractor()

  @Bean
  @Order(10)
  fun axonChannelConfigMessageMetaDataSnippetExtractor(channelConfigs: Map<String, ChannelConfig>, payloadDecoders: List<PayloadDecoder>): MessageMetaDataSnippetExtractor =
    ChannelConfigMessageMetaDataSnippetExtractor(
      channelConfig = requireNotNull(channelConfigs["axon"]) { "Configuration for channel 'axon' is required." },
      payloadDecoders = payloadDecoders
    )
}
