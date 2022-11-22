package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.emptyMessage
import io.holunda.camunda.bpm.correlate.emptyMessageMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AllMessageFilterTest {

  @Test
  fun `should accept all messages`() {
    val filter = AllMessageFilter()
    assertThat(filter.accepts(emptyMessage(), emptyMessageMetadata())).isTrue
  }
}