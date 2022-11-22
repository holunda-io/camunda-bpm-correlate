package io.holunda.camunda.bpm.correlate

import io.holunda.camunda.bpm.correlate.correlation.BatchCorrelationService
import io.holunda.camunda.bpm.correlate.persist.MessagePersistenceService
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService

class CamundaBpmCorrelateServices(
  val configuration: CorrelateConfigurationProperties,
  val messagePersistenceService: MessagePersistenceService,
  val batchCorrelationService: BatchCorrelationService,
  val messageManagementService: MessageManagementService,
  val messageRepository: MessageRepository
)
