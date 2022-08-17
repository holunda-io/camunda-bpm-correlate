In messaging scenarios it is not uncommon that the message bus is transporting more types of message than the current system should consume.
In this case, it is important to filter out and ignore the irrelevant messages and take only the relevant messages into consideration. In the same time,
all messages still needs to be consumed in order not to block the follow-up messages. In order to filter the messages between the [Ingres Adapter](ingres.md)
and the [Message Acceptor](message-acceptor.md) we supply a special `MessageFilter` to filter out the supported messaged.

```kotlin
/**
 * Message filter to filter messages delivered to the message acceptor.
 */
interface MessageFilter {
  /**
   * Checks if the message should be delivered to the message acceptor.
   * @param message message instance
   * @param messageMetaData metadata of the message
   * @return `true` if message should be accepted.
   */
  fun <P> accepts(message: AbstractChannelMessage<P>, messageMetaData: MessageMetaData): Boolean
}
```

Feel free to implement your own filters and supply them as a Spring Bean or choose on of the predefined filter and use them in your setup.

### AllMessageFilter

The `AllMessageFilter` accepts all messages received from the Ingres adapter. 

### AndCompositeMessageFilter

The `AndCompositeMessageFilter` is a composite filter consisting of a list of `MessageFilter` implementations combined by a logical AND operator.

### OrCompositeMessageFilter

The `OrCompositeMessageFilter` is a composite filter consisting of a list of `MessageFilter` implementations combined by a logical OR operator.

### NotMessageFilter

The `NotMessageFilter` is a filter inverting the application of a supplied `MessageFilter` implementation.

### TypeListMessageFilter

The `TypeListMessageFilter` is a filter accepting all messages with a payload type being one of the specified types.

### TypeExistsOnClasspathMessageFilter

The `TypeExistsOnClasspathMessageFilter` is accepting all messages that payload type is available on the class path. 