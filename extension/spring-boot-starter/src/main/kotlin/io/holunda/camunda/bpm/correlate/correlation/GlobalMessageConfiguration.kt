package io.holunda.camunda.bpm.correlate.correlation

import jakarta.annotation.PostConstruct
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

private val logger = KotlinLogging.logger {}
/**
 * Global message configuration.
 */
@AutoConfiguration
@EnableConfigurationProperties(GlobalMessageMetaDataConfigurationProperties::class)
class GlobalMessageConfiguration(private val messageMetaDataConfigurationProperties: GlobalMessageMetaDataConfigurationProperties) {

  /**
   * Prints settings.
   */
  @PostConstruct
  fun info() {
    logger.info { "[Camunda CORRELATE] Message payload encoding: ${messageMetaDataConfigurationProperties.payloadEncoding}" }
    logger.info { "[Camunda CORRELATE] Message TTL: ${messageMetaDataConfigurationProperties.timeToLiveAsString ?: "none"}" }
  }
}
