## Install Dependency

First install the extension using the corresponding ingress adapter (in this example we use Kafka):

```xml

<properties>
  <camunda-bpm-correlate.version>0.0.2</camunda-bpm-correlate.version>
</properties>

<dependencies>
  <dependency>
    <groupId>io.holunda</groupId>
    <artifactId>camunda-bpm-correlate-spring-boot-starter</artifactId>
    <version>${camunda-bpm-correlate.version}</version>
  </dependency>

  <dependency>
    <groupId>io.holunda</groupId>
    <artifactId>camunda-bpm-correlate-spring-cloud-stream</artifactId>
    <version>${camunda-bpm-correlate.version}</version>
  </dependency>

  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka</artifactId>
  </dependency>

</dependencies>
```

## Configuration

Configure your basic Spring Cloud Streams Kafka configuration to looks like this (or similar). Important is 
the name of the function definition.

```yaml

spring:
  cloud:
    stream:
      function:
        definition: streamByteMessageConsumer
        bindings:
          streamByteMessageConsumer-in-0: correlate-ingress-binding      
      bindings:
        correlate-ingres-binding:
          content-type: application/json
          destination: ${KAFKA_TOPIC_CORRELATE_INGRES:correlate-ingress}
          binder: correlate-ingress-binder
          group: ${KAFKA_GROUP_ID}
      binders:
        correlate-ingres-binder:
          type: kafka
          defaultCandidate: false
          inheritEnvironment: false
          environment:
            spring:
              kafka:
                consumer:
                  key-deserializer: org.apache.kafka.common.serialization.ByteArrayDeserializer
                  value-deserializer: org.apache.kafka.common.serialization.ByteArrayDeserializer
              cloud:
                stream:
                  kafka:
                    binder:
                      autoCreateTopics: false
                      autoAddPartitions: false
                      brokers: ${KAFKA_BOOTSTRAP_SERVER_HOST:localhost}:${KAFKA_BOOTSTRAP_SERVER_PORT:9092}
                      configuration:
                        security.protocol: ${KAFKA_SECURITY_PROTOCOL_OVERRIDE:PLAINTEXT}

```

In addition, add the configuration of the extension:

```yaml

correlate:
  enabled: true
  channels:
    stream:
      channelEnabled: true
      message:
        timeToLiveAsString: PT10S # errors during TTL seconds after receiving are ignored
        payloadEncoding: jackson  # our bytes are actually JSON written by Jackson.
  batch:
    mode: all # default fail_first -> 'all' will correlate one message after another, resulting in ignoring the order of receiving
    query:    # query scheduler
      pollInitialDelay: PT10S
      pollInterval: PT6S
    cleanup:  # cleanup of expired messages
      pollInitialDelay: PT1M
      pollInterval: PT1M
  persistence:
    messageMaxRetries: 5 # default 100 -> will try to deliver 5 times at most
    messageFetchPageSize: 100 # default 100
  retry:
    retryMaxBackoffMinutes: 5 # default 180 -> maximum 5 minutes between retries
    retryBackoffBase: 2.0 # value in minutes default 2.0 -> base in the power of retry to calculate the next retry

```
