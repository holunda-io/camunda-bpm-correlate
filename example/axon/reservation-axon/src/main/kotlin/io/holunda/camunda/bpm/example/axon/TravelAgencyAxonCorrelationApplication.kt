package io.holunda.camunda.bpm.example.axon

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.TypePermission
import com.thoughtworks.xstream.security.WildcardTypePermission
import org.axonframework.serialization.xml.XStreamSerializer
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.spring.SpringProcessEnginePlugin
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<TravelAgencyAxonCorrelationApplication>(*args).let { Unit }

@SpringBootApplication
@EnableProcessApplication
class TravelAgencyAxonCorrelationApplication{

  @Bean
  fun disablingTelemetry(): SpringProcessEnginePlugin = object : SpringProcessEnginePlugin() {
    override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl) {
      processEngineConfiguration.isTelemetryReporterActivate = false
      processEngineConfiguration.isInitializeTelemetry = false
    }
  }

  @Bean
  fun xStream() = XStream().apply {
    addPermission(WildcardTypePermission(arrayOf("io.holunda.camunda.bpm.example.**")))
  }
  @Bean
  fun objectMapper(): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)


}
