package io.holunda.camunda.bpm.correlate.ingress.filter

import io.holunda.camunda.bpm.correlate.PayloadType
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.emptyMessage
import io.holunda.camunda.bpm.correlate.emptyMessageMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TypeExistsOnClasspathMessageFilterTest {

  @Test
  fun `should accept any message for a class on a classpath`() {
    val filter = TypeExistsOnClasspathMessageFilter()
    assertThat(filter.accepts(emptyMessage(), emptyMessageMetadata().copy(payloadTypeInfo = TypeInfo.fromClass(PayloadType::class)))).isTrue
    assertThat(filter.accepts(emptyMessage(), emptyMessageMetadata().copy(payloadTypeInfo = TypeInfo.fromFQCN("not.existing.package.NotExistingClass")))).isFalse
  }
}