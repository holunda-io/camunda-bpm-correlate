package io.holunda.camunda.bpm.correlate.correlation.metadata

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.util.*


internal class MessageMetaDataSnippetTest {
  @Test
  fun `reduces adding not-null values`() {

    val messageMetaData = MessageMetaDataSnippet(
      messageId = UUID.randomUUID().toString(),
      messageTimestamp = Instant.now(),
      payloadTypeInfo = TypeInfo.from("namespace", "name", "1"),
      payloadEncoding = "jackson"
    )

    val channelMetaData = MessageMetaDataSnippet(
      timeToLive = Duration.ofHours(2).toString(),
      expiration = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1).toInstant()
    )

    val reduced = MessageMetaDataSnippet.reduce(messageMetaData, channelMetaData)
    assertThat(reduced.messageId).isEqualTo(messageMetaData.messageId)
    assertThat(reduced.messageTimestamp).isEqualTo(messageMetaData.messageTimestamp)
    assertThat(reduced.payloadTypeInfo).isEqualTo(messageMetaData.payloadTypeInfo)
    assertThat(reduced.payloadEncoding).isEqualTo(messageMetaData.payloadEncoding)
    assertThat(reduced.timeToLive).isEqualTo(channelMetaData.timeToLive)
    assertThat(reduced.expiration).isEqualTo(channelMetaData.expiration)
  }

  @Test
  fun `reduces taking values from the second`() {

    val messageMetaData = MessageMetaDataSnippet(
      messageId = UUID.randomUUID().toString(),
      messageTimestamp = Instant.now(),
      payloadTypeInfo = TypeInfo.from("namespace", "name", "1"),
      payloadEncoding = "jackson"
    )

    val channelMetaData = MessageMetaDataSnippet(
      timeToLive = Duration.ofHours(2).toString(),
      expiration = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1).toInstant(),
      messageTimestamp = Instant.now(),
      payloadEncoding = "xml"
    )

    val reduced = MessageMetaDataSnippet.reduce(messageMetaData, channelMetaData)
    assertThat(reduced.messageId).isEqualTo(messageMetaData.messageId)
    assertThat(reduced.messageTimestamp).isEqualTo(channelMetaData.messageTimestamp)
    assertThat(reduced.payloadTypeInfo).isEqualTo(messageMetaData.payloadTypeInfo)
    assertThat(reduced.payloadEncoding).isEqualTo(channelMetaData.payloadEncoding)
    assertThat(reduced.timeToLive).isEqualTo(channelMetaData.timeToLive)
    assertThat(reduced.expiration).isEqualTo(channelMetaData.expiration)
  }

}
