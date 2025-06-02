package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.ingress.cloudstream.SpringCloudStreamChannelConfiguration.Companion.CHANNEL_TYPE
import io.holunda.camunda.bpm.correlate.ingress.cloudstream.SpringCloudStreamChannelConfiguration.Companion.DEFAULT_CHANNEL_MESSAGE_HEADER_CONVERTER
import io.holunda.camunda.bpm.correlate.util.getQualifiedBeanWithFallback
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.support.GenericApplicationContext
import java.util.function.Supplier

private val logger = KotlinLogging.logger {}

/**
 * Factory for creating named channel consumers for cloud streams based on the configuration.
 */
class SpringCloudStreamChannelProxyFactory(
  private val channelMessageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngressMetrics,
  channelConfigurations: Map<String, ChannelConfigurationProperties>,
) : ApplicationContextAware, InitializingBean {

  private lateinit var applicationContext: GenericApplicationContext
  private val springCloudConfigurations: Map<String, ChannelConfigurationProperties> by lazy {
    channelConfigurations.filter { it.value.type == CHANNEL_TYPE && it.value.enabled }
  }

  override fun setApplicationContext(applicationContext: ApplicationContext) {
    this.applicationContext = applicationContext as GenericApplicationContext
  }

  override fun afterPropertiesSet() {
    if (this::applicationContext.isInitialized) {
      logger.debug { "[Camunda CORRELATE] Creating channel consumers for Spring Cloud Streams: ${springCloudConfigurations.keys.joinToString(", ")}." }
      springCloudConfigurations.forEach { (name, config) ->
        // lookup named converter or take the default one
        val converterName = (config.beanNamePrefix ?: name) + "Converter"
        val converter: StreamChannelMessageHeaderConverter = applicationContext.getQualifiedBeanWithFallback(converterName, DEFAULT_CHANNEL_MESSAGE_HEADER_CONVERTER)

        // lookup consumer or create one
        val consumerName = (config.beanNamePrefix ?: name) + "Consumer"
        if (!applicationContext.containsBean(consumerName)) {
          // the channel is not configured yet.
          val consumer = StreamByteMessageConsumer(
            messageAcceptor = channelMessageAcceptor,
            metrics = metrics,
            streamChannelMessageHeaderConverter = converter,
            channelName = name
          )
          applicationContext.registerBean(consumerName, StreamByteMessageConsumer::class.java, Supplier { consumer })
          logger.info { "[Camunda CORRELATE] Registered StreamByteMessageConsumer for channel '$name' named '$consumerName'." }
        } else {
          logger.info { "[Camunda CORRELATE] Found a bean '$consumerName', skipping construction." }
        }
      }
    }
  }

}
