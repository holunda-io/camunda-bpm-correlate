# Camunda BPM Correlate

*Solution for correlation of events with processes.*

[![Build Status](https://github.com/toolisticon/kotlin-lib-template/workflows/Development%20branches/badge.svg)](https://github.com/toolisticon/kotlin-lib-template/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.git/kotlin-lib-template/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.git/kotlin-lib-template)

## Why should you use it?

Imagine you integrate your Camunda Engine into a larger application landscape. In doing so the inter-system communication becomes important and questions on communication styles and patterns arrise. In the world of self-contained systems, the asynchronous communication with messages is wide adopted. This library helps you to solve integration problems around correlation of messages with processes.

## Ideas

- Develop a system accepting the message designed in a store-and-forward way
- Support different messaging source systems (Kafka, Axon, Spring Cloud Streams and others)
- Provide different correlation configurations (message/signal, message ttl, skip correlation/correlation retry)
- Support different persistency (Camunda DB-Schema, ... )
- Crazy but cool (allow INTERNAL correlation use the same library)
- new/error might be different paced... (new - quick, error - exponential back-off)

## MVP

- receive
- write to DB
- job exec (new/error) ->
  - fetch
  - map to vars
  - correlate
  - on error -> write to DB

## Current implementation

channel -> storage

1. `extension/spring-cloud-stream` -> `StreamByteMessageConsumer` is a streaming consumer
2. it requires a header converter (from user code) and calls acceptor
3. this is implemented by `PersistingMessageAcceptorImpl`, which extracts metadata using `MessageMetadataExtractorChain` and persists the message using `MessagePersistenceService`
4. the `MessageMetadataExtractorChain` consists of two extractors: `HeaderMessageMetaDataSnippetExtractor` and a `ChannelConfigurationMessageMetaDataSnippetExtractor`
5. the extractors get metadata from message headers and config from properties
6. the `MessagePersistenceService` uses a `MessageRepository` to store the message (TODO: implement)

storage -> correlation

1. scheduler (TODO: schedlock)
2. calls `DefaultCorrelationService` // TODO: find a better name
3. `DefaultCorrelationService` fetches messages, calculate correlations and selects batches for correlations
4. Every message is decoded using a `PayloadDecoder` and its `CorrelationHint` is determined by the `CorrelationStrategy`
5. Messages with the same `CorrelationHint` build up a batch
6. Every batch is passed to a `BatchCorrelationService` (currently one for Camunda BPM)
7. The correlation delivers a `BatchCorrelationResult` which is either success or error
8. `DefaultCorrelationService` deletes all succeed messages from the batch and updates the error message with information 
retrieved from the `Error`. In order to determine the reaction on the error, the `MessageErrorHandlingStrategy` is used. 
