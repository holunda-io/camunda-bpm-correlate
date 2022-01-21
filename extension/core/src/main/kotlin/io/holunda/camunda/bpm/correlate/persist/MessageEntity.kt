package io.holunda.camunda.bpm.correlate.persist

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "HOL_MESSAGE")
class MessageEntity(
  @Id
  @Column(name = "ID", nullable = false, length = 32)
  val id: String,
  @Column(name = "INSERTED_ON", nullable = false)
  val inserted: Instant,
  @Lob
  @Column(name = "INSIGHT_MESSAGE", nullable = true, length = 1024)
  var payloadJson: String?,
  @Column(name = "RETRIES", nullable = false)
  var retries: Int = 0,
  @Column(name = "NEXT_RETRY_ON", nullable = true)
  var nextRetry: Instant? = null,
  @Lob
  @Column(name = "PROCESSING_ERROR", nullable = true, length = 1024)
  var error: String? = null
)
