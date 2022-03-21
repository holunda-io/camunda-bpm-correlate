package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import org.apache.ibatis.annotations.*

interface MyBatisMessageMapper {

  @Select("SELECT * FROM COR_MESSAGE message")
  @Results(
    value = [
      Result(property = "id", column = "ID"),
      Result(property = "payloadEncoding", column = "PAYLOAD_ENCODING"),
      Result(property = "payloadTypeNamespace", column = "PAYLOAD_TYPE_NAMESPACE"),
      Result(property = "payloadTypeName", column = "PAYLOAD_TYPE_NAME"),
      Result(property = "payloadTypeRevision", column = "PAYLOAD_TYPE_REVISION"),
      Result(property = "payload", column = "PAYLOAD"),
      Result(property = "inserted", column = "INSERTED"),
      Result(property = "timeToLiveDuration", column = "TTL_DURATION"),
      Result(property = "expiration", column = "EXPIRATION"),
      Result(property = "retries", column = "RETRIES"),
      Result(property = "nextRetry", column = "NEXT_RETRY"),
      Result(property = "error", column = "ERROR"),
    ]
  )
  fun findAll(): List<MessageEntity>

  @Select("SELECT * from COR_MESSAGE WHERE ID=#{id}")
  @Results(
    value = [
      Result(property = "id", column = "ID"),
      Result(property = "payloadEncoding", column = "PAYLOAD_ENCODING"),
      Result(property = "payloadTypeNamespace", column = "PAYLOAD_TYPE_NAMESPACE"),
      Result(property = "payloadTypeName", column = "PAYLOAD_TYPE_NAME"),
      Result(property = "payloadTypeRevision", column = "PAYLOAD_TYPE_REVISION"),
      Result(property = "payload", column = "PAYLOAD"),
      Result(property = "inserted", column = "INSERTED"),
      Result(property = "timeToLiveDuration", column = "TTL_DURATION"),
      Result(property = "expiration", column = "EXPIRATION"),
      Result(property = "retries", column = "RETRIES"),
      Result(property = "nextRetry", column = "NEXT_RETRY"),
      Result(property = "error", column = "ERROR"),
    ]
  )
  fun findById(id: String): MessageEntity?

  @Delete("DELETE FROM COR_MESSAGE WHERE ID=#{id}")
  fun deleteById(id: String)

  @Insert("INSERT INTO COR_MESSAGE(ID, PAYLOAD_ENCODING, PAYLOAD_TYPE_NAMESPACE, PAYLOAD_TYPE_NAME, PAYLOAD_TYPE_REVISION, PAYLOAD, INSERTED, TTL_DURATION, EXPIRATION, RETRIES, NEXT_RETRY, ERROR) VALUES (#{id}, #{payloadEncoding}, #{payloadTypeNamespace}, #{payloadTypeName}, #{payloadTypeRevision}, #{payload}, #{inserted}, #{timeToLiveDuration}, #{expiration}, #{retries}, #{nextRetry}, #{error})")
  fun insert(message: MessageEntity)

  @Update("UPDATE COR_MESSAGE SET ID=#{id}, PAYLOAD_ENCODING=#{payloadEncoding}, PAYLOAD_TYPE_NAMESPACE=#{payloadTypeNamespace}, PAYLOAD_TYPE_NAME=#{payloadTypeName}, PAYLOAD_TYPE_REVISION=#{payloadTypeRevision}, PAYLOAD=#{payload}, INSERTED=#{inserted}, TTL_DURATION=#{timeToLiveDuration}, EXPIRATION=#{expiration}, RETRIES=#{retries}, NEXT_RETRY=#{nextRetry}, ERROR=#{error}")
  fun update(message: MessageEntity)

}
