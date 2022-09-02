package io.holunda.camunda.bpm.correlate.persist

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

internal class MessageEntityStatusTest {

  private lateinit var message: MessageEntity

  @BeforeEach
  fun `init message`() {
    message = MessageEntity(
      id = UUID.randomUUID().toString(),
      payloadEncoding = "jackson",
      payloadTypeNamespace = "io.holunda",
      payloadTypeName = "Typea",
      payloadTypeRevision = null,
      payload = ByteArray(0),
      inserted = Instant.now(),
      timeToLiveDuration = null,
      expiration = null
    )
  }


  @Test
  fun `should map status IN_PROGRESS`() {
    assertThat(message.status(10)).isEqualTo(MessageStatus.IN_PROGRESS)
  }

  @Test
  fun `should map status PAUSED`() {
    message.nextRetry = MessageEntity.FAR_FUTURE
    assertThat(message.status(10)).isEqualTo(MessageStatus.PAUSED)
  }

  @Test
  fun `should map status MAX_RETRIES_REACHED`() {
    message.retries = 10
    assertThat(message.status(10)).isEqualTo(MessageStatus.MAX_RETRIES_REACHED)
  }

  @Test
  fun `should map status RETRYING`() {
    message.retries = 2
    message.nextRetry = Instant.now().plus(12, ChronoUnit.MINUTES)
    assertThat(message.status(10)).isEqualTo(MessageStatus.RETRYING)
  }

  @Test
  fun `should map status PAUSED for error`() {
    message.retries = 2
    message.nextRetry = MessageEntity.FAR_FUTURE
    assertThat(message.status(10)).isEqualTo(MessageStatus.PAUSED)
  }

}
