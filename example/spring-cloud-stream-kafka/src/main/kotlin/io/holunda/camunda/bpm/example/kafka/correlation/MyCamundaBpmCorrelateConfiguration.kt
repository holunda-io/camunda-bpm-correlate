package io.holunda.camunda.bpm.example.kafka.correlation

import io.holunda.camunda.bpm.correlate.ingres.cloudstream.SpringCloudStreamChannelConfiguration
import io.holunda.camunda.bpm.correlate.persist.impl.InMemMessageRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.time.Clock

@Configuration
@Import(SpringCloudStreamChannelConfiguration::class)
class MyCamundaBpmCorrelateConfiguration {

  @Bean
  fun messageRepository(clock: Clock) = InMemMessageRepository()
}
