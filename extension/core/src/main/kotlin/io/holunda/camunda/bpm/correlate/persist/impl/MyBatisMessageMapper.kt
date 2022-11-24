package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.CountByStatus
import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import org.apache.ibatis.annotations.*
import org.apache.ibatis.session.RowBounds
import org.apache.ibatis.type.JdbcType
import java.time.Instant

/**
 * Defines a mybatis message mapper.
 */
interface MyBatisMessageMapper {

  /**
   * Finds all messages using paging.
   */
  @Select("SELECT * FROM COR_MESSAGE")
  @Results(
    value = [
      Result(property = "id", column = "ID", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadEncoding", column = "PAYLOAD_ENCODING", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeNamespace", column = "PAYLOAD_TYPE_NAMESPACE", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeName", column = "PAYLOAD_TYPE_NAME", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeRevision", column = "PAYLOAD_TYPE_REVISION", jdbcType = JdbcType.VARCHAR),
      Result(property = "inserted", column = "INSERTED", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "timeToLiveDuration", column = "TTL_DURATION", jdbcType = JdbcType.VARCHAR),
      Result(property = "expiration", column = "EXPIRATION", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "retries", column = "RETRIES", jdbcType = JdbcType.INTEGER),
      Result(property = "nextRetry", column = "NEXT_RETRY", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "error", column = "ERROR", jdbcType = JdbcType.VARCHAR),
    ]
  )
  fun findAllLightPaged(rowBounds: RowBounds): List<MessageEntity>

  /**
   * Find all message light objects (message without payload).
   */
  @Select("SELECT * FROM COR_MESSAGE M WHERE M.ERROR IS NOT NULL")
  @Results(
    value = [
      Result(property = "id", column = "ID", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadEncoding", column = "PAYLOAD_ENCODING", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeNamespace", column = "PAYLOAD_TYPE_NAMESPACE", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeName", column = "PAYLOAD_TYPE_NAME", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeRevision", column = "PAYLOAD_TYPE_REVISION", jdbcType = JdbcType.VARCHAR),
      Result(property = "inserted", column = "INSERTED", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "timeToLiveDuration", column = "TTL_DURATION", jdbcType = JdbcType.VARCHAR),
      Result(property = "expiration", column = "EXPIRATION", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "retries", column = "RETRIES", jdbcType = JdbcType.INTEGER),
      Result(property = "nextRetry", column = "NEXT_RETRY", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "error", column = "ERROR", jdbcType = JdbcType.VARCHAR),
    ]
  )
  fun findAllFaultsLightPaged(rowBounds: RowBounds): List<MessageEntity>

  /**
   * Loads all messages.
   */
  @Select("SELECT * FROM COR_MESSAGE")
  @Results(
    value = [
      Result(property = "id", column = "ID", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadEncoding", column = "PAYLOAD_ENCODING", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeNamespace", column = "PAYLOAD_TYPE_NAMESPACE", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeName", column = "PAYLOAD_TYPE_NAME", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeRevision", column = "PAYLOAD_TYPE_REVISION", jdbcType = JdbcType.VARCHAR),
      Result(property = "payload", column = "PAYLOAD", jdbcType = JdbcType.BINARY),
      Result(property = "inserted", column = "INSERTED", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "timeToLiveDuration", column = "TTL_DURATION", jdbcType = JdbcType.VARCHAR),
      Result(property = "expiration", column = "EXPIRATION", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "retries", column = "RETRIES", jdbcType = JdbcType.INTEGER),
      Result(property = "nextRetry", column = "NEXT_RETRY", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "error", column = "ERROR", jdbcType = JdbcType.VARCHAR),
    ]
  )
  fun findAll(rowBounds: RowBounds): List<MessageEntity>

  /**
   * Finds a message by id.
   */
  @Select("SELECT * from COR_MESSAGE M WHERE M.ID=#{id}")
  @Results(
    value = [
      Result(property = "id", column = "ID"),
      Result(property = "payloadEncoding", column = "PAYLOAD_ENCODING", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeNamespace", column = "PAYLOAD_TYPE_NAMESPACE", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeName", column = "PAYLOAD_TYPE_NAME", jdbcType = JdbcType.VARCHAR),
      Result(property = "payloadTypeRevision", column = "PAYLOAD_TYPE_REVISION", jdbcType = JdbcType.VARCHAR),
      Result(property = "payload", column = "PAYLOAD", jdbcType = JdbcType.BINARY),
      Result(property = "inserted", column = "INSERTED", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "timeToLiveDuration", column = "TTL_DURATION", jdbcType = JdbcType.VARCHAR),
      Result(property = "expiration", column = "EXPIRATION", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "retries", column = "RETRIES", jdbcType = JdbcType.INTEGER),
      Result(property = "nextRetry", column = "NEXT_RETRY", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE),
      Result(property = "error", column = "ERROR", jdbcType = JdbcType.VARCHAR),
    ]
  )
  fun findById(@Param("id") id: String): MessageEntity?

  /**
   * Count messages grouped by status.
   */
  @Select("""
SELECT COUNT(ID)                                                                                                    TOTAL,
       COUNT(CASE WHEN M.ERROR IS NOT NULL THEN 1 END)                                                              ERROR,
       COUNT(CASE WHEN M.ERROR IS NULL AND M.NEXT_RETRY IS NULL THEN 1 END)                                         IN_PROGRESS,
       COUNT(CASE WHEN M.RETRIES = #{maxRetries} THEN 1 END)                                                        MAX_RETRIES_REACHED,
       COUNT(CASE WHEN M.RETRIES > 0 AND M.RETRIES < #{maxRetries} AND M.NEXT_RETRY < #{farFuture} THEN 1 END)      RETRYING,
       COUNT(CASE WHEN M.NEXT_RETRY = #{farFuture} THEN 1 END)                                                      PAUSED
FROM COR_MESSAGE M
""")
  @Results(
    value = [
      Result(property = "total", column = "TOTAL"),
      Result(property = "error", column = "ERROR"),
      Result(property = "inProgress", column = "IN_PROGRESS"),
      Result(property = "maxRetriesReached", column = "MAX_RETRIES_REACHED"),
      Result(property = "retrying", column = "RETRYING"),
      Result(property = "paused", column = "PAUSED"),
    ]
  )
  fun countByStatus(@Param("maxRetries") maxRetries: Int,
                    @Param("now") now: Instant,
                    @Param("farFuture") farFuture: Instant
  ): CountByStatus

  /**
   * Deletes message by id.
   */
  @Delete("DELETE FROM COR_MESSAGE WHERE ID=#{id}")
  fun deleteById(@Param("id") id: String)

  /**
   * Adds a new message.
   */
  @Insert("INSERT INTO COR_MESSAGE(ID, PAYLOAD_ENCODING, PAYLOAD_TYPE_NAMESPACE, PAYLOAD_TYPE_NAME, PAYLOAD_TYPE_REVISION, PAYLOAD, INSERTED, TTL_DURATION, EXPIRATION, RETRIES, NEXT_RETRY, ERROR) VALUES (#{id, jdbcType=VARCHAR}, #{payloadEncoding, jdbcType=VARCHAR}, #{payloadTypeNamespace, jdbcType=VARCHAR}, #{payloadTypeName, jdbcType=VARCHAR}, #{payloadTypeRevision, jdbcType=VARCHAR}, #{payload, jdbcType=BINARY}, #{inserted, jdbcType=TIMESTAMP_WITH_TIMEZONE}, #{timeToLiveDuration, jdbcType=VARCHAR}, #{expiration, jdbcType=TIMESTAMP_WITH_TIMEZONE}, #{retries, jdbcType=INTEGER}, #{nextRetry, jdbcType=TIMESTAMP_WITH_TIMEZONE}, #{error, jdbcType=VARCHAR})")
  fun insert(message: MessageEntity)

  /**
   * Updates a message.
   */
  @Update("UPDATE COR_MESSAGE SET PAYLOAD_ENCODING=#{payloadEncoding, jdbcType=VARCHAR}, PAYLOAD_TYPE_NAMESPACE=#{payloadTypeNamespace, jdbcType=VARCHAR}, PAYLOAD_TYPE_NAME=#{payloadTypeName, jdbcType=VARCHAR}, PAYLOAD_TYPE_REVISION=#{payloadTypeRevision, jdbcType=VARCHAR}, PAYLOAD=#{payload, jdbcType=BINARY}, INSERTED=#{inserted, jdbcType=TIMESTAMP_WITH_TIMEZONE}, TTL_DURATION=#{timeToLiveDuration, jdbcType=VARCHAR}, EXPIRATION=#{expiration, jdbcType=TIMESTAMP_WITH_TIMEZONE}, RETRIES=#{retries, jdbcType=INTEGER}, NEXT_RETRY=#{nextRetry, jdbcType=TIMESTAMP_WITH_TIMEZONE}, ERROR=#{error, jdbcType=VARCHAR} WHERE ID=#{id}")
  fun update(message: MessageEntity)

}
