package io.holunda.camunda.bpm.example.kafka.rest

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.correlate.correlation.BatchCorrelationProcessor
import io.holunda.camunda.bpm.correlate.correlation.metadata.extractor.HeaderMessageMetaDataSnippetExtractor
import io.holunda.camunda.bpm.example.kafka.domain.FlightInfo
import io.holunda.camunda.bpm.example.kafka.domain.FlightReservationReceivedEvent
import io.holunda.camunda.bpm.example.kafka.domain.HotelReservationReceivedEvent
import io.holunda.camunda.bpm.example.kafka.domain.ReservationReceivedEvent
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.support.MessageBuilder
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@RestController
@RequestMapping("/admin")
class AdminRestController(
  val kafkaTemplate: KafkaTemplate<String, ByteArray>,
  val objectMapper: ObjectMapper,
  val batchCorrelationProcessor: BatchCorrelationProcessor
) {

  companion object: KLogging()

  @PostMapping("/start")
  fun startProcess(): ResponseEntity<Void> {
    val event = ReservationReceivedEvent(
      reservationId = UUID.randomUUID().toString(),
      customerName = "Chuck Norris",
      from = OffsetDateTime.now().plusDays(2),
      to = OffsetDateTime.now().plusDays(4),
      fromCity = "Hamburg",
      toCity = "Berlin"
    )
    val payload = objectMapper.writeValueAsBytes(event)

    logger.info { "Payload: ${String(payload)}" }

    val now = Instant.now().let { "${it.epochSecond}${it.nano}" }
    val message = MessageBuilder
      .withPayload(payload)
      .setHeaders(MessageHeaderAccessor().also {
      it.setHeader(HeaderMessageMetaDataSnippetExtractor.HEADER_MESSAGE_ID.name, now)
      it.setHeader(HeaderMessageMetaDataSnippetExtractor.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME.name, ReservationReceivedEvent::class.java.name)
    }).build()

    kafkaTemplate.send(message)
    return noContent().build()
  }

  @PostMapping("/flightReservationReceived")
  fun flightReservationReceived(): ResponseEntity<Void> {
    val now = Instant.now().let { "${it.epochSecond}${it.nano}" }
    val event = FlightReservationReceivedEvent(
      passengersName = "Chuck Norris",
      outgoingFlight = FlightInfo(
        fromAirport = "HAM",
        toAirport = "BER",
        flightNumber = "LH-001",
        seat = "10C",
        departure = OffsetDateTime.now().plusDays(2)
      ),
      incomingFlight = FlightInfo(
        fromAirport = "BER",
        toAirport = "HAM",
        flightNumber = "LH-002",
        seat = "17A",
        departure = OffsetDateTime.now().plusDays(4)
      )
    )
    val payload = objectMapper.writeValueAsBytes(event)


    val message = MessageBuilder
      .withPayload(payload)
      .setHeaders(MessageHeaderAccessor().also {
        it.setHeader(HeaderMessageMetaDataSnippetExtractor.HEADER_MESSAGE_ID.name, now)
        it.setHeader(HeaderMessageMetaDataSnippetExtractor.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME.name, FlightReservationReceivedEvent::class.java.name)
      }).build()

    kafkaTemplate.send(message)
    return noContent().build()
  }

  @PostMapping("/hotelReservationReceived")
  fun hotelReservationReceived(): ResponseEntity<Void> {
    val event = HotelReservationReceivedEvent(
      guestName = "Chuck Norris",
      roomNumber = "37",
      arrival = OffsetDateTime.now().plusDays(2).truncatedTo(ChronoUnit.DAYS),
      departure = OffsetDateTime.now().plusDays(4).truncatedTo(ChronoUnit.DAYS),
    )
    val payload = objectMapper.writeValueAsBytes(event)
    val now = Instant.now().let { "${it.epochSecond}${it.nano}" }
    val message = MessageBuilder
      .withPayload(payload)
      .setHeaders(MessageHeaderAccessor().also {
        it.setHeader(HeaderMessageMetaDataSnippetExtractor.HEADER_MESSAGE_ID.name, now)
        it.setHeader(HeaderMessageMetaDataSnippetExtractor.HEADER_MESSAGE_PAYLOAD_TYPE_CLASS_NAME.name, HotelReservationReceivedEvent::class.java.name)
      }).build()

    kafkaTemplate.send(message)
    return noContent().build()
  }


  @PostMapping("/correlate")
  fun correlate(): ResponseEntity<Void> {
    batchCorrelationProcessor.correlate()
    return noContent().build()
  }

}

