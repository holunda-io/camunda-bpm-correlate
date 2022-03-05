package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageAcceptorConfiguration
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceConfiguration
import org.springframework.context.annotation.Import

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(
  CamundaBpmCorrelateConfiguration::class,
  ChannelMessageAcceptorConfiguration::class,
  MessagePersistenceConfiguration::class
)
annotation class EnableCamundaBpmCorrelate
