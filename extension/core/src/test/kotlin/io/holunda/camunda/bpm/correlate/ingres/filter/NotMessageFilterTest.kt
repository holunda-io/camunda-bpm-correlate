package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.acceptingFilter
import io.holunda.camunda.bpm.correlate.emptyMessage
import io.holunda.camunda.bpm.correlate.emptyMessageMetadata
import io.holunda.camunda.bpm.correlate.rejectingFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NotMessageFilterTest {

  @Test
  fun `should invert filter application`() {
    assertThat(NotMessageFilter(rejectingFilter()).accepts(emptyMessage(), emptyMessageMetadata())).isTrue
    assertThat(NotMessageFilter(acceptingFilter()).accepts(emptyMessage(), emptyMessageMetadata())).isFalse

    assertThat(
      NotMessageFilter(
        NotMessageFilter(
          rejectingFilter()
        )
      ).accepts(emptyMessage(), emptyMessageMetadata())
    ).isEqualTo(
      rejectingFilter(
      ).accepts(emptyMessage(), emptyMessageMetadata())
    )
  }
}