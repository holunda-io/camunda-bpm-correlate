package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptorConfiguration
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Spring clouf stream channel configuration.
 */
@Configuration
@AutoConfigureAfter(ChannelMessageAcceptorConfiguration::class)
class SpringCloudStreamChannelConfiguration {

  companion object {
    const val CHANNEL_TYPE = "stream"
    const val DEFAULT_CHANNEL_MESSAGE_HEADER_CONVERTER = "channelMessageHeaderConverter"
  }

  /**
   * Create message header converter.
   */
  @ConditionalOnMissingBean
  @Bean(DEFAULT_CHANNEL_MESSAGE_HEADER_CONVERTER)
  @Qualifier(DEFAULT_CHANNEL_MESSAGE_HEADER_CONVERTER)
  fun channelMessageHeaderConverter(): StreamChannelMessageHeaderConverter = KafkaStreamChannelMessageHeaderConverter()

  /**
   * Initialize channel factory.
   */
  @Bean
  fun springCloudStreamChannelProxyFactory(
    channelMessageAcceptor: ChannelMessageAcceptor,
    metrics: IngressMetrics,
    channelConfigurations: Map<String, ChannelConfigurationProperties>
  ) = SpringCloudStreamChannelProxyFactory(
    channelMessageAcceptor = channelMessageAcceptor,
    metrics = metrics,
    channelConfigurations = channelConfigurations
  )

}
