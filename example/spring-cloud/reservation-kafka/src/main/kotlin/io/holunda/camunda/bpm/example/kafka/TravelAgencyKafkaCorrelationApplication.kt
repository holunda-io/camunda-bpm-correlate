package io.holunda.camunda.bpm.example.kafka

import io.holunda.camunda.bpm.correlate.correlation.BatchConfigurationProperties
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.spring.SpringProcessEnginePlugin
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<TravelAgencyKafkaCorrelationApplication>(*args).let { Unit }

@SpringBootApplication
@EnableProcessApplication
class TravelAgencyKafkaCorrelationApplication{

  @Bean
  fun disablingTelemetry(): SpringProcessEnginePlugin = object : SpringProcessEnginePlugin() {
    override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl) {
      processEngineConfiguration.isTelemetryReporterActivate = false
      processEngineConfiguration.isInitializeTelemetry = false
    }
  }

}
