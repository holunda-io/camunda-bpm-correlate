# Camunda BPM Correlate

*Solution for correlation of events with processes.*

[![Build Status](https://github.com/toolisticon/kotlin-lib-template/workflows/Development%20branches/badge.svg)](https://github.com/toolisticon/kotlin-lib-template/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.git/kotlin-lib-template/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.git/kotlin-lib-template)
![Compatible with: Camunda Platform 7](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%207-26d07c)

## Why should you use it?

Imagine you integrate your Camunda process engine into a larger application landscape. 
In doing so the inter-system communication becomes important and questions on 
communication styles and patterns arise. In the world of self-contained systems, 
the asynchronous communication with messages is wide adopted. This library helps 
you to solve integration problems around correlation of messages with processes.

## Main Features

* Ingres adapters for:
    * Spring Cloud Streams (e.g. Kafka Streams, Rabbit MQ, Azure Event Hubs, AWS SQS, AWS SNS, Solace PubSub+, Google PubSub)
* Inbox pattern on message receiving
* Message storage in the Camunda Platform 7 database
    * MyBatis repository 
* Asynchronous scheduled batch-mode correlation
* Variety of error handling modes on mismatched correlation
* Configurable timings, retry strategies and many other parameters

## License

[![Apache License 2](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

This library is developed under Apache 2.0 License.

## Contribution

If you want to contribute to this project, feel free to do so. 
Start with [Contributing guide](http://holunda.io/camunda-bpm-correlate/snapshot/developer-guide/contribution.html).

