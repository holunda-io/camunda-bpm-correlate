package io.holunda.camunda.bpm.correlate.ingres.axon

import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.xstream.XStream
import io.holunda.camunda.bpm.correlate.EnableCamundaBpmCorrelate
import io.holunda.camunda.bpm.correlate.correlation.metadata.MessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfig
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.ChannelConfigMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptor
import io.holunda.camunda.bpm.correlate.ingres.IngresMetrics
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer
import org.axonframework.serialization.xml.XStreamSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
@EnableCamundaBpmCorrelate
@ConditionalOnProperty(value = ["correlate.channels.axon.channelEnabled"], havingValue = "true", matchIfMissing = false)
class AxonChannelConfiguration {

  companion object {
    const val SERIALIZER = "messageCorrelateSerializer"
  }

  @Bean
  @Qualifier(SERIALIZER)
  @ConditionalOnProperty(value = ["correlate.channels.axon.payloadEncoding"], havingValue = "jackson", matchIfMissing = false)
  fun messageCorrelateJacksonSerializer(objectMapper: ObjectMapper): Serializer =
    JacksonSerializer
      .builder()
      .lenientDeserialization()
      .objectMapper(objectMapper)
      .build()

  @Bean
  @Qualifier(SERIALIZER)
  @ConditionalOnProperty(value = ["correlate.channels.axon.payloadEncoding"], havingValue = "xstream", matchIfMissing = false)
  fun messageCorrelateXStreamSerializer(xStream: XStream): Serializer =
    XStreamSerializer
      .builder()
      .lenientDeserialization()
      .xStream(xStream)
      .build()

  @ConditionalOnMissingBean
  @Bean
  fun axonEventHandler(
    channelMessageAcceptor: ChannelMessageAcceptor,
    metrics: IngresMetrics,
    axonEventHeaderExtractor: AxonEventHeaderExtractor,
    @Qualifier(SERIALIZER) serializer: Serializer
  ) = AxonEventMessageHandler(
    messageAcceptor = channelMessageAcceptor,
    metrics = metrics,
    axonEventHeaderExtractor = axonEventHeaderExtractor,
    serializer = serializer
  )

  @ConditionalOnMissingBean
  @Bean
  fun axonEventHeaderExtractor(channelConfigs: Map<String, ChannelConfig>): AxonEventHeaderExtractor =
    DefaultAxonEventHeaderExtractor()

  @Bean
  @Order(10)
  fun axonChannelConfigMessageMetaDataSnippetExtractor(channelConfigs: Map<String, ChannelConfig>): MessageMetaDataSnippetExtractor =
    ChannelConfigMessageMetaDataSnippetExtractor(
      channelConfig = requireNotNull(channelConfigs["axon"]) { "Configuration for channel 'axon' is required." }
    )

}
