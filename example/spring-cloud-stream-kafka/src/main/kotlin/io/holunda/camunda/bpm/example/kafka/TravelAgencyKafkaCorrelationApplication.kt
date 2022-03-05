package io.holunda.camunda.bpm.example.kafka

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) = runApplication<TravelAgencyKafkaCorrelationApplication>(*args).let { Unit }

@SpringBootApplication
@EnableProcessApplication
class TravelAgencyKafkaCorrelationApplication
