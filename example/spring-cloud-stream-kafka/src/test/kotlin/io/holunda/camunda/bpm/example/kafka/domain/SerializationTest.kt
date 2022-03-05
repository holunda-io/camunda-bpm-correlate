package io.holunda.camunda.bpm.example.kafka.domain

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.*

internal class SerializationTest {

  private val mapper = jacksonObjectMapper()
    .findAndRegisterModules()
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  @Test
  fun `serialize and deserialize`() {

    val event = ReservationReceivedEvent(
      reservationId = UUID.randomUUID().toString(),
      customerName = "Chuck Norris",
      from = OffsetDateTime.now().plusDays(2),
      to = OffsetDateTime.now().plusDays(4),
      fromCity = "Hamburg",
      toCity = "Berlin"
    )

    val type = mapper.typeFactory.constructFromCanonical(
      ReservationReceivedEvent::class.java.name
    )

    val jsonString = mapper.writeValueAsString(event)
    val backFromString = mapper.readValue<ReservationReceivedEvent>(jsonString, type)
    assertThat(event.customerName).isEqualTo(backFromString.customerName)

    val jsonBytes = mapper.writeValueAsBytes(event)
    val backFromBytes = mapper.readValue<ReservationReceivedEvent>(jsonBytes, type)
    assertThat(event.customerName).isEqualTo(backFromBytes.customerName)
  }
}
