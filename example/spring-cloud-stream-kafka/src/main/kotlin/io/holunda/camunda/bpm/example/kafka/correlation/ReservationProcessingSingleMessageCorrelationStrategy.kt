package io.holunda.camunda.bpm.example.kafka.correlation

import io.holunda.camunda.bpm.correlate.correlation.CorrelationMessage
import io.holunda.camunda.bpm.correlate.correlation.SingleMessageCorrelationStrategy
import io.holunda.camunda.bpm.correlate.correlation.impl.MessageIdCorrelationMessageComparator
import io.holunda.camunda.bpm.correlate.event.CorrelationHint
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Companion.KEY
import io.holunda.camunda.bpm.example.kafka.ReservationProcessingFactoryBean.Variables.CUSTOMER_NAME
import io.holunda.camunda.bpm.example.kafka.domain.FlightReservationReceivedEvent
import io.holunda.camunda.bpm.example.kafka.domain.HotelReservationReceivedEvent
import io.holunda.camunda.bpm.example.kafka.domain.ReservationReceivedEvent
import org.camunda.bpm.engine.RepositoryService
import org.springframework.stereotype.Component

@Component
class ReservationProcessingSingleMessageCorrelationStrategy(
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

  override fun correlationSelector(): (CorrelationMessage) -> CorrelationHint {
    return { message ->
      when (val payload = message.payload) {
        is ReservationReceivedEvent -> CorrelationHint(
          processDefinitionId = processDefinitionId,
          processStart = true,
        )
        is FlightReservationReceivedEvent -> CorrelationHint(
          correlationVariables = mapOf(CUSTOMER_NAME.name to payload.passengersName)
        )
        is HotelReservationReceivedEvent -> CorrelationHint(
          correlationVariables = mapOf(CUSTOMER_NAME.name to payload.guestName)
        )
        else -> throw IllegalArgumentException("Could not determine correlation hint for message ${message.messageMetaData}")
      }
    }
  }

  override fun correlationMessageSorter(): Comparator<CorrelationMessage> = MessageIdCorrelationMessageComparator()
}
