package io.holunda.camunda.bpm.correlate.event

import mu.KLogger
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoMoreInteractions

internal class CorrelationHintTest {

  private val logger: KLogger = mock()

  @Test
  fun `should create a valid correlation hint`() {
    val hint = CorrelationHint(
      businessKey = "business-key",
      processDefinitionId = "def-id",
      processStart = true
    )

    hint.sanityCheck(logger, CorrelationScope.GLOBAL, CorrelationType.MESSAGE)
    verifyNoMoreInteractions(logger)
  }

}