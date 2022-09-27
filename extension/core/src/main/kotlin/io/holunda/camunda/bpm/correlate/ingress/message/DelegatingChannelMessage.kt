package io.holunda.camunda.bpm.correlate.ingress.message

/**
 * Message implementation based on a delegate-pattern using a payload supplier function to retrieve the payload on access.
 * @param delegate message to delegate to.
 * @param payloadSupplier payload supplier function retrieving the payload from the delegate.
 * @param D payload type of delegate
 * @param P payload type of the message
 */
class DelegatingChannelMessage<P : Any, D : Any>(
  val delegate: ChannelMessage<D>,
  val payloadSupplier: (D) -> P
) : ChannelMessage<P> {

  override val headers: Map<String, Any>
    get() = delegate.headers

  override val payload: P
    get() = payloadSupplier.invoke(delegate.payload)
}