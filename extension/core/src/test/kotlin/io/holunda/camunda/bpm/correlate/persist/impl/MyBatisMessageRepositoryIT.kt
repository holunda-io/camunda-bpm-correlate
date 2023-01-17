package io.holunda.camunda.bpm.correlate.persist.impl

import io.holunda.camunda.bpm.correlate.persist.MessageEntity
import io.holunda.camunda.bpm.correlate.persist.MessageEntity.Companion.FAR_FUTURE
import org.apache.ibatis.session.SqlSessionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant


@ExtendWith(SpringExtension::class)
@ActiveProfiles("itest")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyBatisMessageRepositoryIT {

  @Autowired
  private lateinit var repository: MyBatisMessageRepository

  companion object {
    val MESSAGES_IDS_BY_INSERTED_ASC = listOf("4711", "4716", "4712", "4713", "4714", "4715")
    val MESSAGES_IDS_BY_INSERTED_ASC_FAULTS = listOf("4713", "4714", "4715")
  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `selects one by id`() {

    val message = repository.findByIdOrNull("4711")
    assertThat(message).isNotNull.apply {
      extracting( MessageEntity::id.name ).isEqualTo("4711")
      extracting( MessageEntity::payloadTypeName.name ).isEqualTo("MyType")
      extracting( MessageEntity::payloadTypeNamespace.name ).isEqualTo("io.holixon.namespace")
      extracting( MessageEntity::payloadEncoding.name ).isEqualTo("JACKSON")
    }

    val nothing = repository.findByIdOrNull("does-not-exist")
    assertThat(nothing).isNull()

  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `count by status`() {
    val now = Instant.parse("2022-09-01T10:00:00Z")

    val count = repository.countByStatus(3, now, FAR_FUTURE)
    assertThat(count.total).isEqualTo(6)
    assertThat(count.error).isEqualTo(3)
    assertThat(count.retrying).isEqualTo(1)
    assertThat(count.inProgress).isEqualTo(2)
    assertThat(count.maxRetriesReached).isEqualTo(1)
    assertThat(count.paused).isEqualTo(2)
  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `select paged in correct order`() {
    val messages = repository.findAll(0, 100);
    assertThat(messages.map { it.id }).containsExactlyElementsOf(MESSAGES_IDS_BY_INSERTED_ASC)
  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `select light in correct order`() {
    val messages = repository.findAllLight(0, 100, false);
    assertThat(messages.map { it.id }).containsExactlyElementsOf(MESSAGES_IDS_BY_INSERTED_ASC)
  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `select light faults in correct order`() {
    val messages = repository.findAllLight(0, 100, true);
    assertThat(messages.map { it.id }).containsExactlyElementsOf(MESSAGES_IDS_BY_INSERTED_ASC_FAULTS)
  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `select second page in correct order`() {
    val messages = repository.findAll(1, 3)
    assertThat(messages.map { it.id }).containsExactlyElementsOf(MESSAGES_IDS_BY_INSERTED_ASC.subList(3, MESSAGES_IDS_BY_INSERTED_ASC.size))
  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `select light second page in correct order`() {
    val messages = repository.findAllLight(1, 3, false)
    assertThat(messages.map { it.id }).containsExactlyElementsOf(MESSAGES_IDS_BY_INSERTED_ASC.subList(3, MESSAGES_IDS_BY_INSERTED_ASC.size))
  }

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `select light faults second page in correct order`() {
    val messages = repository.findAllLight(1, 2, true);
    assertThat(messages.map { it.id }).containsExactlyElementsOf(MESSAGES_IDS_BY_INSERTED_ASC_FAULTS.subList(2, MESSAGES_IDS_BY_INSERTED_ASC_FAULTS.size))
  }



  @org.springframework.context.annotation.Configuration
  class MyConfiguration {

    @Bean
    fun myBatisMessageRepository(sqlSessionFactory: SqlSessionFactory): MyBatisMessageRepository = MyBatisMessageRepository(sqlSessionFactory.apply {
      this.configuration.mapperRegistry.addMapper(MyBatisMessageMapper::class.java)
    })
  }
}