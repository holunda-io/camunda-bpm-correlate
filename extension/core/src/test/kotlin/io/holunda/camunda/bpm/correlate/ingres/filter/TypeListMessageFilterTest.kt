package io.holunda.camunda.bpm.correlate.ingres.filter

import io.holunda.camunda.bpm.correlate.PayloadType
import io.holunda.camunda.bpm.correlate.PayloadType2
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo
import io.holunda.camunda.bpm.correlate.emptyMessage
import io.holunda.camunda.bpm.correlate.emptyMessageMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TypeListMessageFilterTest {

  @Test
  fun `should match exact type by class name`() {

    val filter = TypeListMessageFilter(PayloadType::class.qualifiedName!!)

    val metaData = emptyMessageMetadata().copy(payloadTypeInfo = TypeInfo.fromClass(PayloadType::class))

    assertThat(filter.accepts(emptyMessage(), metaData)).isTrue
    assertThat(filter.accepts(emptyMessage(), emptyMessageMetadata())).isFalse
  }

  @Test
  fun `should match one of types type by class name`() {

    val filter = TypeListMessageFilter(PayloadType::class.qualifiedName!!, PayloadType2::class.qualifiedName!!)

    val metaData = emptyMessageMetadata().copy(payloadTypeInfo = TypeInfo.fromClass(PayloadType::class))

    assertThat(filter.accepts(emptyMessage(), metaData)).isTrue
    assertThat(filter.accepts(emptyMessage(), emptyMessageMetadata())).isFalse
  }

  @Test
  fun `should match one of type by class`() {

    class InnerClassWithoutFullQualifiedName

    val filter = TypeListMessageFilter(setOf(PayloadType::class.java, PayloadType2::class.java, InnerClassWithoutFullQualifiedName::class.java))

    val metaData = emptyMessageMetadata().copy(payloadTypeInfo = TypeInfo.fromClass(PayloadType::class))

    assertThat(filter.accepts(emptyMessage(), metaData)).isTrue
    assertThat(filter.accepts(emptyMessage(), emptyMessageMetadata())).isFalse
  }

}