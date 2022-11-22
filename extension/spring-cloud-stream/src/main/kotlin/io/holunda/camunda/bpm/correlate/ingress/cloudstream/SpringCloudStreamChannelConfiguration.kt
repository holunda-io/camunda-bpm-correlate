package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptorConfiguration
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@AutoConfigureAfter(ChannelMessageAcceptorConfiguration::class)
class SpringCloudStreamChannelConfiguration {

  companion object {
    const val CHANNEL_TYPE = "stream"
  }

  @ConditionalOnMissingBean
  @Bean
  fun channelMessageHeaderConverter(): StreamChannelMessageHeaderConverter = KafkaStreamChannelMessageHeaderConverter()

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
