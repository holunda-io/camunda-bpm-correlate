package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.ingress.cloudstream.SpringCloudStreamChannelConfiguration.Companion.CHANNEL_TYPE
import io.holunda.camunda.bpm.correlate.util.getQualifiedBeanWithFallback
import mu.KLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.support.GenericApplicationContext
import java.util.function.Supplier

class SpringCloudStreamChannelProxyFactory(
  private val channelMessageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngressMetrics,
  channelConfigurations: Map<String, ChannelConfigurationProperties>,
) : ApplicationContextAware, InitializingBean {

  companion object : KLogging()

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
        val consumerName = config.beanName ?: "$name-consumer"
        if (!applicationContext.containsBean(consumerName)) {
          // the channel is not configured yet.
          val consumer = StreamByteMessageConsumer(
            messageAcceptor = channelMessageAcceptor,
            metrics = metrics,
            streamChannelMessageHeaderConverter = applicationContext.getQualifiedBeanWithFallback(name),
            channelName = name
          )
          applicationContext.registerBean(consumerName, StreamByteMessageConsumer::class.java, Supplier { consumer })
          logger.info { "[Camunda CORRELATE] Registered StreamByteMessageConsumer for channel '$name' named '$consumerName'." }
        }
      }
    }
  }

}