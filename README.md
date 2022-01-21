# Camunda BPM Correlate

*Solution for correlation of events with processes.*

[![Build Status](https://github.com/toolisticon/kotlin-lib-template/workflows/Development%20branches/badge.svg)](https://github.com/toolisticon/kotlin-lib-template/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.git/kotlin-lib-template/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.git/kotlin-lib-template)

## Why should you use it?

Imagine you integrate your Camunda Engine into a larger application landscape. In doing so the inter-system communication becomes important and questions on communication styles and patterns arrise. In the world of self-contained systems, the asynchronous communication with messages is wide adopted. This library helps you to solve integration problems around correlation of messages with processes.

## Ideas

- Develop a system accepting the message designed in a store-and-forward way
- Support different messaging source systems (Kafka, Axon, Spring Boot Streams and others)
- Provide different correlation configurations (message/signal, message ttl, skip correlation/correlation retry)
- Support different persistencies (Camunda DB-Schema, ... )
- Crazy but cool (allow INTERNAL correlation use the same library)

## MVP

- receive
- write to DB
- job exec (new/error) ->
  - fetch
  - map to vars
  - correlate
  - on error -> write to DB

new/error might be different paced... (new - quick, error - exponential back-off)
