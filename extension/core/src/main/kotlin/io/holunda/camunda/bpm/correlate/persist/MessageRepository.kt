package io.holunda.camunda.bpm.correlate.persist

import org.springframework.data.repository.PagingAndSortingRepository

/**
 * JPA Message Repository.
 */
interface MessageRepository : PagingAndSortingRepository<MessageEntity, String>
