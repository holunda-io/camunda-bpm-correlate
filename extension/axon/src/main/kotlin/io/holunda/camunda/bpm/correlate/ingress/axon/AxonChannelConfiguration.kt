package io.holunda.camunda.bpm.correlate.ingress.axon

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.GlobalConfig
import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptorConfiguration
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * Axon Framework Channel configuration.
 */
@AutoConfiguration
@AutoConfigureAfter(
  AxonAutoConfiguration::class,
  ChannelMessageAcceptorConfiguration::class
)
class AxonChannelConfiguration {

  companion object {
    const val CHANNEL_TYPE = "axon-event"
    const val PROPERTY_CHANNEL_PAYLOAD_ENCODING = "payload-encoding"
    const val DEFAULT_MESSAGE_HEADER_CONVERTER_NAME = "axonEventMessageHeaderConverter"
  }

  /**
   * Channel header extractor.
   */
  @ConditionalOnMissingBean
  @Bean(DEFAULT_MESSAGE_HEADER_CONVERTER_NAME)
  fun axonEventMessageHeaderConverter(): AxonEventMessageHeaderConverter {
    return DefaultAxonEventMessageHeaderConverter()
  }

  /**
   * Configuration of named channels.
   */
  @Bean
  fun axonChannelProxyFactory(
    channelMessageAcceptor: ChannelMessageAcceptor,
    metrics: IngressMetrics,
    channelConfigurations: Map<String, ChannelConfigurationProperties>,
    payloadDecoders: List<PayloadDecoder>,
    globalConfig: GlobalConfig,
  ) = AxonChannelProxyFactory(
    channelMessageAcceptor = channelMessageAcceptor,
    metrics = metrics,
    channelConfigurations = channelConfigurations,
    payloadDecoders = payloadDecoders,
    globalConfig = globalConfig
  )
}
