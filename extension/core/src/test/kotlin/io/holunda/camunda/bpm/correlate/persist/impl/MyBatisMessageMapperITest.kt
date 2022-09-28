package io.holunda.camunda.bpm.correlate.persist.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@MybatisTest
@SpringBootApplication
@Disabled
class MyBatisMessageMapperITest {

  @Autowired
  private lateinit var mapper: MyBatisMessageMapper

  @Test
  fun `selects elements`() {

    val message = mapper.findById("1")
    assertThat(message).isNotNull
  }
}