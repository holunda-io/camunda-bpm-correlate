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
import org.springframework.context.annotation.Lazy
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
  classes = [AxonChannelConfigurationIT.TestApplication::class],
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  properties = [
    // axon-1
    "correlate.channels.axonOne.type=axon-event",
    "correlate.channels.axonOne.enabled=true",
    // axon-2
    "correlate.channels.axon-2.type=axon-event",
    "correlate.channels.axon-2.enabled=true",
    "correlate.channels.axon-2.beanNamePrefix=specified",
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
  @Lazy
  private lateinit var handlers: Map<String, AxonEventMessageHandler>

  @Autowired
  @Qualifier("specifiedConverter")
  private lateinit var converter: AxonEventMessageHeaderConverter


  @Test
  fun configures_two_consumers() {

    assertThat(handlers).hasSize(2)
    assertThat(handlers.keys).containsExactlyInAnyOrder("axonOneHandler", "specifiedHandler")
    assertThat(handlers["axonOneHandler"]!!.channelName).isEqualTo("axonOne")
    assertThat(handlers["specifiedHandler"]!!.channelName).isEqualTo("axon-2")
    assertThat(handlers["specifiedHandler"]!!.axonEventMessageHeaderConverter).isEqualTo(converter)
  }

  @SpringBootApplication(exclude = [BatchCorrelationSchedulerConfiguration::class])
  class TestApplication {
    @Bean("specifiedConverter")
    fun doesNotMatter(): AxonEventMessageHeaderConverter = mock()

    @Bean
    fun singleMessageCorrelationStrategy(): SingleMessageCorrelationStrategy = mock()
  }
}
