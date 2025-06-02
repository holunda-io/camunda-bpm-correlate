package io.holunda.camunda.bpm.example.kafka.correlation.correlate.rest

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Admin controller.
 */
@RequestMapping("/admin")
class AdminRestController(
  val messageManagementService: MessageManagementService
) {

  /**
   * Allows to check the inbox messages.
   */
  @GetMapping("/list-messages")
  fun correlate(): ResponseEntity<List<MessageEntity>> {
    return ok(messageManagementService.listAllMessages())
  }

}

