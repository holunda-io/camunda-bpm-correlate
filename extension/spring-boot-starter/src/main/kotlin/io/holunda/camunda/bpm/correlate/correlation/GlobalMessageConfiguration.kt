package io.holunda.camunda.bpm.correlate.correlation

import jakarta.annotation.PostConstruct
import mu.KLogging
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

/**
 * Global message configuration.
 */
@AutoConfiguration
@EnableConfigurationProperties(GlobalMessageMetaDataConfigurationProperties::class)
class GlobalMessageConfiguration(private val messageMetaDataConfigurationProperties: GlobalMessageMetaDataConfigurationProperties) {

  companion object : KLogging()

  /**
   * Prints settings.
   */
  @PostConstruct
  fun info() {
    logger.info { "[Camunda CORRELATE] Message payload encoding: ${messageMetaDataConfigurationProperties.payloadEncoding}" }
    logger.info { "[Camunda CORRELATE] Message TTL: ${messageMetaDataConfigurationProperties.timeToLiveAsString ?: "none"}" }
  }
}
