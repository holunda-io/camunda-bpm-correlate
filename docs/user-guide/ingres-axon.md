The Axon Framework Ingres Adapter is responsible for receiving events on Axon Event bus and sending them for the correlation to the library. 

### Message

Axon Event Message is received and deserialized by Axon Framework, using the configured message de-serializer and passed to the ingres adapter.
The adapter is reading headers from message `MetaData` and converts them into message headers. The payload is encoded into serializable payload using 
the configured encoder (currently Jackson).
