package io.holunda.camunda.bpm.correlate.persist.datajpa

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import org.springframework.data.repository.CrudRepository

interface JpaMessageRepository: CrudRepository<MessageEntity, String>, MessageRepository {
}
