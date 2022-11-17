package io.holunda.camunda.bpm.correlate.ingress.cloudstream

import io.holunda.camunda.bpm.correlate.correlation.BatchCorrelationSchedulerConfiguration
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
  classes = [SpringCloudStreamChannelConfigurationIT.TestApplication::class],
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  properties = [
    // kafka-1
    "correlate.channels.kafka-1.type=stream",
    "correlate.channels.kafka-1.enabled=true",
    // kafka-2
    "correlate.channels.kafka-2.type=stream",
    "correlate.channels.kafka-2.enabled=true",
    "correlate.channels.kafka-2.beanName=specified-consumer-name",
    // unknown-type
    "correlate.channels.unknown-type.type=unknown-type",
    "correlate.channels.unknown-type.enabled=true",
    // disabled
    "correlate.channels.disabled.type=stream",
    "correlate.channels.disabled.enabled=false",

    // function declaration
    "spring.cloud.stream.function.definition=kafka-1-consumer; specified-consumer-name",
    // bindings
    "spring.cloud.stream.function.bindings.kafka-1-consumer-in-0=correlate-ingress-binding-1",
    "spring.cloud.stream.function.bindings.specified-consumer-name=correlate-ingress-binding-1",
  ]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("spring-cloud-stream")
@EmbeddedKafka(partitions = 1, count = 1, ports = [59092], topics = ["correlate-ingress"])
internal class SpringCloudStreamChannelConfigurationIT {

  @Autowired
  private lateinit var consumers: Map<String, StreamByteMessageConsumer>

  @Autowired
  @Qualifier("specified-consumer-name")
  private lateinit var converter: StreamChannelMessageHeaderConverter


  @Test
  fun configures_two_consumers() {

    assertThat(consumers).hasSize(2)
    assertThat(consumers.keys).containsExactlyInAnyOrder("kafka-1-consumer", "specified-consumer-name")
    assertThat(consumers["kafka-1-consumer"]!!.channelName).isEqualTo("kafka-1")
    assertThat(consumers["specified-consumer-name"]!!.channelName).isEqualTo("kafka-2")
    assertThat(consumers["specified-consumer-name"]!!.streamChannelMessageHeaderConverter).isEqualTo(converter)
  }

  @SpringBootApplication(exclude = [BatchCorrelationSchedulerConfiguration::class])
  class TestApplication {
    @Bean
    @Qualifier("specified-consumer-name")
    fun qualifiedConverter(): StreamChannelMessageHeaderConverter = mock()

    @Bean
    fun singleMessageCorrelationStrategy(): SingleMessageCorrelationStrategy = mock()
  }
}