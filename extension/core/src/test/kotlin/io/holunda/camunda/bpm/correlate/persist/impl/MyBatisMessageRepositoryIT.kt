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

  @Test
  @Sql(scripts = ["/some-messages.sql"])
  fun `selects one by id`() {

    val message = repository.findByIdOrNull("4711")
    assertThat(message).isNotNull.apply {
      extracting( MessageEntity::id.name ).isEqualTo("4711")
      extracting( MessageEntity::payloadTypeName.name ).isEqualTo("MyType")
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


  @org.springframework.context.annotation.Configuration
  class MyConfiguration {

    @Bean
    fun myBatisMessageRepository(sqlSessionFactory: SqlSessionFactory): MyBatisMessageRepository = MyBatisMessageRepository(sqlSessionFactory.apply {
      this.configuration.mapperRegistry.addMapper(MyBatisMessageMapper::class.java)
    })
  }
}