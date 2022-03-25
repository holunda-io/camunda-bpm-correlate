package io.holunda.camunda.bpm.example.kafka

import io.holunda.camunda.bpm.correlate.ingres.cloudstream.SpringCloudStreamChannelConfiguration
import io.holunda.camunda.bpm.example.kafka.correlation.UsingCamundaCorrelateConfiguration
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.spring.SpringProcessEnginePlugin
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

fun main(args: Array<String>) = runApplication<TravelAgencyKafkaCorrelationApplication>(*args).let { Unit }

@SpringBootApplication
@EnableProcessApplication
@Import(SpringCloudStreamChannelConfiguration::class) // FIXME: should use annotation and the channel should activate based on property.
class TravelAgencyKafkaCorrelationApplication {

  @Bean
  fun disablingTelemetry(): SpringProcessEnginePlugin = object : SpringProcessEnginePlugin() {
    override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl) {
      processEngineConfiguration.isTelemetryReporterActivate = false
      processEngineConfiguration.isInitializeTelemetry = false
    }
  }
}
