package io.holunda.camunda.bpm.correlate.correlation

import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import mu.KLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@Configuration
@EnableScheduling
class BatchCorrelationSchedulerConfiguration(
  private val batchCorrelationProcessor: BatchCorrelationProcessor,
  private val messageCleanupService: MessageManagementService,
  private val batchConfigurationProperties: BatchConfigurationProperties
) : SchedulingConfigurer {

  companion object : KLogging()

  // FIXME: implement better scheduler

  @Scheduled(
    initialDelayString = "#{batchConfigurationProperties.queryPollInitialDelay}",
    fixedRateString = "#{batchConfigurationProperties.queryPollInterval}"
  )
  fun runCorrelation() {
    batchCorrelationProcessor.correlate()
    val remaining = messageCleanupService.listAllMessages()
    logger.info { "There are ${remaining.size} messages in the message table." }
    remaining.forEach {
      logger.debug { "The message is : $it" }
    }
  }

  @Scheduled(
    initialDelayString = "#{batchConfigurationProperties.cleanupPollInitialDelay}",
    fixedRateString = "#{batchConfigurationProperties.cleanupPollInterval}"
  )
  fun cleanupExpired() {
    messageCleanupService.cleanupExpired()
  }

  @Bean(destroyMethod = "shutdown")
  @Qualifier("correlateTaskExecutor")
  fun correlateTaskExecutor(): Executor {
    return Executors.newScheduledThreadPool(100)
  }

  override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
// TODO: https://stackoverflow.com/questions/14630539/scheduling-a-job-with-spring-programmatically-with-fixedrate-set-dynamically
  }
}
