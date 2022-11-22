Currently, the library supports the following features:

## General

* Ingress Adapters:
    * Spring Cloud Kafka
    * Axon Framework
* MetaData extractors:
    * Message based (Headers)
    * Channel based (Properties)
* Persisting Message Accepting Adapter
* Message Persistence
    * In-Memory
    * MyBatis (using the same DB as Camunda Platform 7)
* Batch processor running on schedule
    * Batch modes: all, fail-first
* Correlation error strategies: ignore, drop, retry
* Message Buffering (TTL)
* Message Expiry
