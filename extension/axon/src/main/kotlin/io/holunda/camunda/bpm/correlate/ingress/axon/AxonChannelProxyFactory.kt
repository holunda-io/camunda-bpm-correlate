package io.holunda.camunda.bpm.correlate.ingress.axon

import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.GlobalConfig
import io.holunda.camunda.bpm.correlate.ingress.ChannelConfigurationProperties
import io.holunda.camunda.bpm.correlate.ingress.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingress.IngressMetrics
import io.holunda.camunda.bpm.correlate.ingress.axon.AxonChannelConfiguration.Companion.CHANNEL_TYPE
import io.holunda.camunda.bpm.correlate.ingress.axon.AxonChannelConfiguration.Companion.PROPERTY_CHANNEL_PAYLOAD_ENCODING
import io.holunda.camunda.bpm.correlate.persist.encoding.PayloadDecoder
import io.holunda.camunda.bpm.correlate.util.getQualifiedBeanWithFallback
import mu.KLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.support.GenericApplicationContext
import java.util.function.Supplier

/**
 * Spring factory creating Axon Framework channels based on configuration.
 */
class AxonChannelProxyFactory(
  private val channelMessageAcceptor: ChannelMessageAcceptor,
  private val metrics: IngressMetrics,
  private val payloadDecoders: List<PayloadDecoder>,
  private val globalConfig: GlobalConfig,
  channelConfigurations: Map<String, ChannelConfigurationProperties>
) : ApplicationContextAware, InitializingBean {

  companion object : KLogging()

  private lateinit var applicationContext: GenericApplicationContext
  private val axonEventConfigurations: Map<String, ChannelConfigurationProperties> by lazy {
    channelConfigurations.filter { it.value.type == CHANNEL_TYPE && it.value.enabled }
  }

  override fun setApplicationContext(applicationContext: ApplicationContext) {
    this.applicationContext = applicationContext as GenericApplicationContext
  }

  override fun afterPropertiesSet() {
    if (this::applicationContext.isInitialized) {
      logger.debug { "[Camunda CORRELATE] Creating channel consumers for Axon Event Bus: ${axonEventConfigurations.keys.joinToString(", ")}." }
      axonEventConfigurations.forEach { (name, config) ->

        val encoding: String = requireNotNull( getEncoding(config) ) { "Channel encoding is required, please set either globally or for channel." }
        val encoder = requireNotNull(payloadDecoders.find { it.supports(encoding) }) { "Could not find decoder for configured message encoding '$encoding'." }

        val handlerName = config.beanName ?: "$name-handler"
        if (!applicationContext.containsBean(handlerName)) {
          // the channel is not configured yet.
          val handler = AxonEventMessageHandler(
            messageAcceptor = channelMessageAcceptor,
            metrics = metrics,
            axonEventMessageHeaderConverter = applicationContext.getQualifiedBeanWithFallback(name),
            encoder = encoder,
            channelName = name
          )
          applicationContext.registerBean(handlerName, AxonEventMessageHandler::class.java, Supplier { handler })
          logger.info { "[Camunda CORRELATE] Registered AxonEventMessageHandler for channel '$name' named '$handlerName'." }
        }
      }
    }
  }

  private fun getEncoding(config: ChannelConfigurationProperties): String? {
    return config.properties.getOrDefault(PROPERTY_CHANNEL_PAYLOAD_ENCODING, globalConfig.getMessagePayloadEncoding())?.toString()
  }

}
