# Message acceptor

The message acceptor is a component responsible to receive the message from the Ingres adapter and store it into the database. 

## Message Metadata
Along with the payload the message must contain metadata represented by the `MessageMetadata`. This metadata is extracted from the channel settings, 
message payload and other sources. Usually the `MessageMetadata` instance can't be constructed at one place, so we supply the `MessageMetaDataExtractorChain`
consisting of `MessageMetaDataSnippetExtractor` instances. By doing so, every aspect of metadata extraction is put in it own class and the result of the 
extraction is gathered in one `MessageMetadata` instance.

To simplify the construction of `MessageMetaDataExtractorChain` we supply several `MessageMetaDataSnippetExtractor` implementations:

### ChannelConfigMessageMetaDataSnippetExtractor

The `ChannelConfigMessageMetaDataSnippetExtractor` is responsible for reading metadata from the channel configuration. Usually, channel configuration parameters
like message encoding are extracted that way.

### HeaderMessageMessageMetaDataSnippetExtractor

The `HeaderMessageMessageMetaDataSnippetExtractor` is responsible for reading metadata from message headers. Most communication technologies support some concept
of message headers and the corresponding [Ingres Adapter](./ingres-axon.md) is mapping those headers to message headers used in the library. By doing so, 
you can influence message attributes on a message level. For example, if you are receiving different types of messages by the same ingres adapter, this is 
the easiest way to detect the type of the message.

## Message filtering

The `FilteringMessageMessageMetaDataSnippetExtractor` is not responsible for any metadata extraction but is used as a filter for incoming messages. You can
configure what messages it will support and filter the messages that will be delivered to the message acceptor.


## Persisting Channel Message Acceptor

To implement the inbox pattern, the message acceptor stores received messages in a persistent storage. 