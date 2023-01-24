package io.holunda.camunda.bpm.example.kafka.correlation.correlate

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.correlation.impl.IdentityMessageComparator
import io.holunda.camunda.bpm.correlate.correlation.impl.MessageIdCorrelationMessageComparator
import io.holunda.camunda.bpm.correlate.event.CorrelationHint
import io.holunda.camunda.bpm.correlate.persist.impl.DefaultMessagePersistenceService
import io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
import io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
import io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.KEY
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.ReservationProcessing.Variables.RESERVATION_ID
import org.camunda.bpm.engine.RepositoryService

/**
 * Correlation config.
 */
class ReservationProcessingCorrelation(
  val repositoryService: RepositoryService
) : SingleMessageCorrelationStrategy {

  private val processDefinitionId by lazy {
    requireNotNull(
      repositoryService
        .createProcessDefinitionQuery()
        .processDefinitionKey(KEY)
        .active()
        .latestVersion()
        .singleResult()
    ) { "Reservation process with key $KEY is not deployed" }.id
  }

  /**
   * Targeting the workflow instance.
   */
  override fun correlationSelector(): (CorrelationMessage) -> CorrelationHint {
    return { message ->
      when (val payload = message.payload) {
        is ReservationReceivedEvent -> CorrelationHint(
          processDefinitionId = processDefinitionId,
          processStart = true,
        )
        is FlightReservationConfirmedEvent -> CorrelationHint(
          correlationVariables = mapOf(
            CUSTOMER_NAME.name to payload.passengersName,
            RESERVATION_ID.name to payload.bookingReference
          )
        )
        is HotelReservationConfirmedEvent -> CorrelationHint(
          correlationVariables = mapOf(
            CUSTOMER_NAME.name to payload.guestName,
            RESERVATION_ID.name to payload.bookingReference
          )
        )
        is DefaultMessagePersistenceService.PayloadNotAvailable -> throw IllegalArgumentException("No payload was available, could not determine correlation hint for message ${message.messageMetaData}")
        else -> throw IllegalArgumentException("Could not determine correlation hint for message ${message.messageMetaData}")
      }
    }
  }

  override fun correlationMessageSorter(): Comparator<CorrelationMessage> = IdentityMessageComparator()
}
