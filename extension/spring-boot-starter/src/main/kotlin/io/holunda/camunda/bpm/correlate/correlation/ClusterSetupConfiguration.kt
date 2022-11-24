package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceConfiguration
import mu.KLogging
import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import javax.annotation.PostConstruct
import javax.sql.DataSource

/**
 * Cluster setup configuration.
 */
@ConditionalOnProperty(value = ["correlate.batch.cluster.enabled"], havingValue = "true", matchIfMissing = false)
@AutoConfigureAfter(MessagePersistenceConfiguration::class)
@EnableSchedulerLock(defaultLockAtMostFor = "PT10M") // TODO, make to some sort of a parameter
class ClusterSetupConfiguration {

  companion object : KLogging()

  /**
   * Reports cluster config activation.
   */
  @PostConstruct
  fun printUsage() {
    logger.info { "[Camunda CORRELATE] Cluster configuration is activated." }
  }

  /**
   * Configures lock provider for the scheduler.
   */
  @Bean
  fun messageCorrelationLockProvider(dataSource: DataSource): LockProvider {
    return JdbcTemplateLockProvider(
      JdbcTemplateLockProvider.Configuration.builder()
        .withJdbcTemplate(JdbcTemplate(dataSource))
        .usingDbTime()
        .build()
    )
  }
}
