package io.holunda.camunda.bpm.example.axon.correlate.rest

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.impl.MessageManagementService
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Admin controller to see the messages.
 */
@RequestMapping("/admin")
class AdminRestController(
  val messageManagementService: MessageManagementService
) {

  companion object : KLogging()

  /**
   * Provides a direct insight to messages in the inbox.
   */
  @GetMapping("/list-messages")
  fun correlate(): ResponseEntity<List<MessageEntity>> {
    return ok(messageManagementService.listAllMessages())
  }

}

