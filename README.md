# Camunda BPM Correlate

*Solution for correlation of events with processes.*

[![incubating](https://img.shields.io/badge/lifecycle-INCUBATING-orange.svg)](https://github.com/holisticon#open-source-lifecycle)
[![Build Status](https://github.com/holunda-io/camunda-bpm-correlate/workflows/Development%20branches/badge.svg)](https://github.com/holunda-io/camunda-bpm-correlate/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.holunda/camunda-bpm-correlate/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.holunda/camunda-bpm-correlate)
![Compatible with: Camunda Platform 7](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%207-26d07c)
[![codecov](https://codecov.io/gh/holunda-io/camunda-bpm-correlate/branch/develop/graph/badge.svg?token=EWjlAeLt8v)](https://codecov.io/gh/holunda-io/camunda-bpm-correlate)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b751121e82a6432d90a8844725dc9af7)](https://www.codacy.com/gh/holunda-io/camunda-bpm-correlate/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=holunda-io/camunda-bpm-correlate&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/b751121e82a6432d90a8844725dc9af7)](https://www.codacy.com/gh/holunda-io/camunda-bpm-correlate/dashboard?utm_source=github.com&utm_medium=referral&utm_content=holunda-io/camunda-bpm-correlate&utm_campaign=Badge_Coverage)


# Relocation

This project is **relocated** to https://github.com/holunda-io/c7 and **will be developed** further there.
Last release produced from this location was **2025.05.1**
This repository will be archived.

## Why should you use it?

Imagine you integrate your Camunda process engine into a larger application landscape. 
In doing so the inter-system communication becomes important and questions on 
communication styles and patterns arise. In the world of self-contained systems, 
the asynchronous communication with messages is wide adopted. This library helps 
you to solve integration problems around correlation of messages with processes.

## Main Features

* Ingress adapters for:
    * Spring Cloud Streams (e.g. Kafka Streams, Rabbit MQ, Azure Event Hubs, AWS SQS, AWS SNS, Solace PubSub+, Google PubSub)
    * Axon Framework (Axon Event Bus)
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

