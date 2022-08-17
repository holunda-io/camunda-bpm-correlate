package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.acceptingFilter
import io.holunda.camunda.bpm.correlate.emptyMessage
import io.holunda.camunda.bpm.correlate.emptyMessageMetadata
import io.holunda.camunda.bpm.correlate.rejectingFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OrCompositeMessageFilterTest {

  @Test
  fun `should reject if none filter is accepting`() {
    val orFilter = OrCompositeMessageFilter(listOf(
      rejectingFilter(), rejectingFilter(), rejectingFilter()
    ))
    assertThat(orFilter.accepts(emptyMessage(), emptyMessageMetadata())).isFalse
  }

  @Test
  fun `should accept if one filter is accepting`() {

    val orFilter = OrCompositeMessageFilter(listOf(
      rejectingFilter(), rejectingFilter(), rejectingFilter(), acceptingFilter()
    ))
    assertThat(orFilter.accepts(emptyMessage(), emptyMessageMetadata())).isTrue
  }
}