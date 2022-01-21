package io.holunda.camunda.bpm.correlate.persist

import io.holunda.camunda.bpm.correlate.ingres.AbstractGenericMessage

class MessagePersistenceService(
  val messageRepository: MessageRepository
) {

  fun <P, M : AbstractGenericMessage<P>> persistMessage(message: M) {

  }
}
