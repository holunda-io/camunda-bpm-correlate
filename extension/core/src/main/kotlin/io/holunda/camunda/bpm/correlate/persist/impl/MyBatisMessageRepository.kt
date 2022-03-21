package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageRepository
import mu.KLogging
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.transaction.TransactionFactory

/**
 * MyBatis implementation of the message repository.
 */

class MyBatisMessageRepository(
  private val sqlSessionFactory: SqlSessionFactory
) : MessageRepository {

  companion object: KLogging()

  override fun findAll(pageSize: Int): List<MessageEntity> {
    return sqlSessionFactory.openSession().use {
      it.getMapper(MyBatisMessageMapper::class.java).findAll()
    }
  }

  override fun findByIdOrNull(id: String): MessageEntity? {
    return sqlSessionFactory.openSession().use {
      it.getMapper(MyBatisMessageMapper::class.java).findById(id)
    }
  }

  override fun save(message: MessageEntity) {
    return sqlSessionFactory.openSession().use {
      val mapper = it.getMapper(MyBatisMessageMapper::class.java)
      val existing = mapper.findById(message.id)
      if (existing == null) {
        mapper.insert(message)
      } else {
        mapper.update(message)
      }
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
