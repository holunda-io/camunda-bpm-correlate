package io.holunda.camunda.bpm.example.kafka.ingres

import io.holunda.camunda.bpm.correlate.ingres.ChannelMessageHeaderConverter
import org.springframework.stereotype.Component

@Component
class KafkaMessageHeaderConverter : ChannelMessageHeaderConverter {

  override fun convertChannelHeaders(channelHeaders: Map<String, Any>): Map<String, Any> {
    return channelHeaders
  }
}
