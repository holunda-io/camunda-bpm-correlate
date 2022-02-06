package io.holunda.camunda.bpm.correlate.persist

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "HOL_MESSAGE")
class MessageEntity(

  @Column(name = "ID", nullable = false, length = 32)
  @Id
  var id: String,
  @Column(name = "INSERTED_ON", nullable = false)
  var inserted: Instant,

  @Column(name = "PAYLOAD_ENCODING", nullable = false, length = 64)
  var payloadEncoding: String,

  @Column(name = "PAYLOAD_TYPE_NAMESPACE", nullable = false, length = 64)
  var payloadTypeNamespace: String,
  @Column(name = "PAYLOAD_TYPE_NAME", nullable = false, length = 64)
  var payloadTypeName: String,
  @Column(name = "PAYLOAD_TYPE_REVISION", nullable = true, length = 64)
  var payloadTypeRevision: String?,

  @Column(name = "PAYLOAD", nullable = true, length = 1024)
  @Lob
  var payload: ByteArray,

  @Column(name = "TTL", nullable = true, length = 16)
  var timeToLive: String?,
  @Column(name = "RETRIES", nullable = false)
  var retries: Int = 0,
  @Column(name = "NEXT_RETRY_ON", nullable = true)
  var nextRetry: Instant? = null,
  @Column(name = "PROCESSING_ERROR", nullable = true, length = 1024)
  @Lob
  var error: String? = null
)
