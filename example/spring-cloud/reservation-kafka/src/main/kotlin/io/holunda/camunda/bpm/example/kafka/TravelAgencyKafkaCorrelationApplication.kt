package io.holunda.camunda.bpm.example.kafka

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.spring.SpringProcessEnginePlugin
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.*
import javax.annotation.PostConstruct

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

  @PostConstruct
  fun init() {
    // Setting Spring Boot SetTimeZone
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
  }

}
