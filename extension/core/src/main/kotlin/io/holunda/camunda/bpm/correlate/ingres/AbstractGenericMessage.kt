package io.holunda.camunda.bpm.correlate.ingres

abstract class AbstractGenericMessage<P>(
  val headers: Map<String, Any>,
  val payload: P
) {
  fun getPayloadType(): Class<out P> = payload!!::class.java
}
