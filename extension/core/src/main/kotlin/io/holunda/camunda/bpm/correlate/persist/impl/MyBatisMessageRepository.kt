package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import mu.KLogging
import org.apache.ibatis.session.RowBounds
import org.apache.ibatis.session.SqlSessionFactory
import java.time.Instant

/**
 * MyBatis implementation of the message repository.
 */

class MyBatisMessageRepository(
  private val sqlSessionFactory: SqlSessionFactory
) : MessageRepository {

  companion object : KLogging()

  override fun findAllLight(page: Int, pageSize: Int, faultsOnly: Boolean): List<MessageEntity> {
    return sqlSessionFactory.openSession().use {
      if (faultsOnly) {
        it.getMapper(MyBatisMessageMapper::class.java).findAllFaultsLightPaged(RowBounds(pageSize * page, pageSize))
      } else {
        it.getMapper(MyBatisMessageMapper::class.java).findAllLightPaged(RowBounds(pageSize * page, pageSize))
      }
    }
  }

  override fun findAll(page: Int, pageSize: Int): List<MessageEntity> {
    return sqlSessionFactory.openSession().use {
      it.getMapper(MyBatisMessageMapper::class.java).findAll(RowBounds(pageSize * page, pageSize))
    }
  }

  override fun findByIdOrNull(id: String): MessageEntity? {
    return sqlSessionFactory.openSession().use {
      it.getMapper(MyBatisMessageMapper::class.java).findById(id)
    }
  }

  override fun countByStatus(maxRetries: Int, now: Instant, farFuture: Instant): CountByStatus {
    return sqlSessionFactory.openSession().use {
      it.getMapper(MyBatisMessageMapper::class.java).countByStatus(maxRetries, now, farFuture)
    }
  }


  override fun insert(message: MessageEntity) {
    return sqlSessionFactory.openSession().use {
      val mapper = it.getMapper(MyBatisMessageMapper::class.java)
      val existing = mapper.findById(message.id)
      require(existing == null) { "Message with id ${message.id} already exists." }
      mapper.insert(message)
      it.commit()
    }
  }

  override fun save(message: MessageEntity) {
    return sqlSessionFactory.openSession().use {
      val mapper = it.getMapper(MyBatisMessageMapper::class.java)
      val existing = mapper.findById(message.id)
      requireNotNull(existing) { "Could not find message with id ${message.id}" }
      mapper.update(message)
      it.commit()
    }
  }

  override fun deleteAllById(ids: List<String>) {
    return sqlSessionFactory.openSession().use { it ->
      val mapper = it.getMapper(MyBatisMessageMapper::class.java)
      ids.forEach {
        mapper.deleteById(it)
      }
      it.commit()
    }
  }
}
