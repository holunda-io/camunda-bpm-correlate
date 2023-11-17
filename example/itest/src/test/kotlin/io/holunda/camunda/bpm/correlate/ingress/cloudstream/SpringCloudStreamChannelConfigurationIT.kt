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
    "correlate.channels.kafkaOne.type=stream",
    "correlate.channels.kafkaOne.enabled=true",
    // kafka-2
    "correlate.channels.kafkaTwo.type=stream",
    "correlate.channels.kafkaTwo.enabled=true",
    "correlate.channels.kafkaTwo.beanNamePrefix=specifiedName",
    // unknown-type
    "correlate.channels.unknownType.type=unknown-type",
    "correlate.channels.unknownType.enabled=true",
    // disabled
    "correlate.channels.disabled.type=stream",
    "correlate.channels.disabled.enabled=false",
    // function declaration
    "spring.cloud.stream.function.definition=kafkaOneConsumer; specifiedNameConsumer",
    // bindings
    "spring.cloud.stream.function.bindings.kafkaOneConsumer-in-0=correlate-ingress-binding-1",
    "spring.cloud.stream.function.bindings.specifiedNameConsumer-in-0=correlate-ingress-binding-1",

  ]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("spring-cloud-stream")
@EmbeddedKafka(partitions = 1, count = 1, ports = [59092], topics = ["correlate-ingress"])
internal class SpringCloudStreamChannelConfigurationIT {

  @Autowired
  private lateinit var consumers: Map<String, StreamByteMessageConsumer>

  @Autowired
  @Qualifier("specifiedNameConverter")
  private lateinit var converter: StreamChannelMessageHeaderConverter // this converter will be picked up because of the name of the field

  @Test
  fun configures_two_consumers() {

    assertThat(consumers).hasSize(2)
    assertThat(consumers.keys).containsExactlyInAnyOrder("kafkaOneConsumer", "specifiedNameConsumer")
    assertThat(consumers["kafkaOneConsumer"]!!.channelName).isEqualTo("kafkaOne")
    assertThat(consumers["specifiedNameConsumer"]!!.channelName).isEqualTo("kafkaTwo")
    assertThat(consumers["specifiedNameConsumer"]!!.streamChannelMessageHeaderConverter).isEqualTo(converter)
  }

  @SpringBootApplication(exclude = [BatchCorrelationSchedulerConfiguration::class])
  class TestApplication {

    @Bean("specifiedNameConverter")
    fun qualifiedConverter(): StreamChannelMessageHeaderConverter = mock()

    @Bean
    fun singleMessageCorrelationStrategy(): SingleMessageCorrelationStrategy = mock()
  }
}
