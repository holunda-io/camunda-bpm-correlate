package io.holunda.camunda.bpm.correlate.ingres.message

/**
 * Message implementation based on a delegate-pattern using a payload supplier function to retrieve the payload on access.
 */
class DelegatingChannelMessage<P : Any>(
  val delegate: ChannelMessage<Any>,
  val payloadSupplier: (Any) -> P
) : ChannelMessage<P> {

  override val headers: Map<String, Any>
    get() = delegate.headers

  override val payload: P
    get() = payloadSupplier.invoke(delegate.payload)
}