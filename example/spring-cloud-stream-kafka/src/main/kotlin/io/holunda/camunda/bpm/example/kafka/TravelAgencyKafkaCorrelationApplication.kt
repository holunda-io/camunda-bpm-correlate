package io.holunda.camunda.bpm.example.kafka

import io.holunda.camunda.bpm.correlate.ingres.cloudstream.SpringCloudStreamChannelConfiguration
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

fun main(args: Array<String>) = runApplication<TravelAgencyKafkaCorrelationApplication>(*args).let { Unit }

@SpringBootApplication
@EnableProcessApplication
@Import(SpringCloudStreamChannelConfiguration::class) // FIXME: should use annotation and the channel should activate based on property.
class TravelAgencyKafkaCorrelationApplication
