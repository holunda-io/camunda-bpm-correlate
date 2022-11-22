package io.holunda.camunda.bpm.correlate.ingress.axon

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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
  classes = [AxonChannelConfigurationIT.TestApplication::class],
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  properties = [
    // axon-1
    "correlate.channels.axon-1.type=axon-event",
    "correlate.channels.axon-1.enabled=true",
    // axon-2
    "correlate.channels.axon-2.type=axon-event",
    "correlate.channels.axon-2.enabled=true",
    "correlate.channels.axon-2.beanName=specified-handler-name",
    // unknown-type
    "correlate.channels.unknown-type.type=unknown-type",
    "correlate.channels.unknown-type.enabled=true",
    // disabled
    "correlate.channels.disabled.type=stream",
    "correlate.channels.disabled.enabled=false",
  ]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("axon-event")
internal class AxonChannelConfigurationIT {

  @Autowired
  private lateinit var handlers: Map<String, AxonEventMessageHandler>

  @Autowired
  @Qualifier("specified-handler-name")
  private lateinit var converter: AxonEventMessageHeaderConverter


  @Test
  fun configures_two_consumers() {

    assertThat(handlers).hasSize(2)
    assertThat(handlers.keys).containsExactlyInAnyOrder("axon-1-handler", "specified-handler-name")
    assertThat(handlers["axon-1-handler"]!!.channelName).isEqualTo("axon-1")
    assertThat(handlers["specified-handler-name"]!!.channelName).isEqualTo("axon-2")
    assertThat(handlers["specified-handler-name"]!!.axonEventMessageHeaderConverter).isEqualTo(converter)
  }

  @SpringBootApplication(exclude = [BatchCorrelationSchedulerConfiguration::class])
  class TestApplication {
    @Bean
    @Qualifier("specified-handler-name")
    fun qualifiedConverter(): AxonEventMessageHeaderConverter = mock()

    @Bean
    fun singleMessageCorrelationStrategy(): SingleMessageCorrelationStrategy = mock()
  }
}
