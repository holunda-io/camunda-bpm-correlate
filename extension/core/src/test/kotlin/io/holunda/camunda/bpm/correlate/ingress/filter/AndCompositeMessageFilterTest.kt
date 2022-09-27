package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.acceptingFilter
import io.holunda.camunda.bpm.correlate.emptyMessage
import io.holunda.camunda.bpm.correlate.emptyMessageMetadata
import io.holunda.camunda.bpm.correlate.rejectingFilter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class AndCompositeMessageFilterTest {

  @Test
  fun `should accept if all filters are accepting`() {
    val orFilter = AndCompositeMessageFilter(
      listOf(
        acceptingFilter(), acceptingFilter(), acceptingFilter()
      )
    )
    Assertions.assertThat(orFilter.accepts(emptyMessage(), emptyMessageMetadata())).isTrue
  }

  @Test
  fun `should reject if at least one filter is rejecting`() {
    val orFilter = AndCompositeMessageFilter(
      listOf(
        acceptingFilter(), acceptingFilter(), rejectingFilter()
      )
    )
    Assertions.assertThat(orFilter.accepts(emptyMessage(), emptyMessageMetadata())).isFalse
  }

}