package io.holunda.camunda.bpm.correlate.dto

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime

class RetriesDtoDeserializationTest {

  private val objectMapper: ObjectMapper = ObjectMapper().apply {
    registerModule(KotlinModule.Builder().build())
    registerModule(JavaTimeModule())
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
  }

  @Test
  fun `should serialize and deserialize`() {
    val now = ZonedDateTime.now(ZoneOffset.UTC)
    val original = RetriesDto(
      nextRetry = now,
      retries = 42
    )

    val json = objectMapper.writeValueAsString(original)
    println(json)
    assertThat(json).isEqualTo("{\"retries\":${original.retries},\"nextRetry\":\"${now}\"}")


    val dto = objectMapper.readValue(json, RetriesDto::class.java)
    assertThat(dto.nextRetry).isEqualTo(original.nextRetry)
    assertThat(dto.retries).isEqualTo(original.retries)
  }

  @Test
  fun `should deserialize if timezone is not set`() {

    val now = "2022-11-02T13:09:13.391234Z"
    val dto = objectMapper.readValue("{\"retries\":42,\"nextRetry\":\"${now}\"}", RetriesDto::class.java)
    assertThat(dto.nextRetry).isEqualTo(ZonedDateTime.parse(now))
    assertThat(dto.retries).isEqualTo(42)

  }

}