The Spring Cloud Ingres Adapter is a component responsible for receiving Spring Cloud messages (using configured binding like Kafka, AMQP or others) and 
convert them into message format used by the library.

### Message

Kafka Message is received and the Kafka headers are converted to message headers. 
