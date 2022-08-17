The messages received and accepted by the [message acceptor](message-acceptor.md) are stored in a relation database. For doing so, we implemented
a MyBatis mapper of the underlying entity in order to keep the dependency track as small as possible (MyBatis is a library used and supplied by Camunda Platform 7).
For the persistence of the messages the library uses a database table `COR_MESSAGE` with the following structure:

| Column                 | Java Datatype | JDBC Datatype           | Description                                                   |
|------------------------|---------------|-------------------------|---------------------------------------------------------------|
| ID                     | String        | VARCHAR                 | Message id (unique)                                           | 
| PAYLOAD_ENCODING       | String        | VARCHAR                 | Encoding of the payload                                       | 
| PAYLOAD_TYPE_NAMESPACE | String        | VARCHAR                 | Namespace of the payload type, for example package            | 
| PAYLOAD_TYPE_NAME      | String        | VARCHAR                 | Simple type name of the payload type, for example class name  | 
| PAYLOAD_TYPE_REVISION  | String        | VARCHAR                 | Revision of the payload type.                                 | 
| PAYLOAD                | ByteArray     | BINARY                  | Byte array containing the encoded payload                     | 
| INSERTED               | Instant       | TIMESTAMP WITH TIMEZONE | Timestamp of message ingestion                                | 
| TTL_DURATION           | String        | VARCHAR                 | Time to live of the message as Duration string                | 
| EXPIRATION             | Instant       | TIMESTAMP WITH TIMEZONE | Expiration of the message as timestamp                        | 
| RETRIES                | Integer       | INTEGER                 | Number of retries of message correlation                      | 
| NEXT_RETRY             | Instant       | TIMESTAMP WITH TIMEZONE | Timestamp of the next retry                                   | 
| ERROR                  | String        | VARCHAR                 | Last error stacktrace produced during correlation             | 

Depending on your database you will need different SQL DDLs to create the underlying DB table. Here are some dialects, we already tried out:

### MS SQL / Azure SQL

We use `NVARCHAR` as a basic type for strings because of improved index performance for UTF-8 encoded strings.

```tsql
CREATE TABLE COR_MESSAGE (
    ID                     NVARCHAR(64) UNIQUE NOT NULL,
    PAYLOAD_ENCODING       NVARCHAR(64)        NOT NULL,
    PAYLOAD_TYPE_NAMESPACE NVARCHAR(128)       NOT NULL,
    PAYLOAD_TYPE_NAME      NVARCHAR(128)       NOT NULL,
    PAYLOAD_TYPE_REVISION  NVARCHAR(64),
    PAYLOAD                BINARY(4096),
    INSERTED               DATETIME2           NOT NULL,
    TTL_DURATION           NVARCHAR(32),
    EXPIRATION             DATETIME2,
    RETRIES                INT                 NOT NULL,
    NEXT_RETRY             DATETIME2,
    ERROR                  NVARCHAR(MAX)
);
```

### H2 / HSQL

```h2
CREATE TABLE COR_MESSAGE (
    ID                     VARCHAR2(64) UNIQUE NOT NULL,
    PAYLOAD_ENCODING       VARCHAR2(64)        NOT NULL,
    PAYLOAD_TYPE_NAMESPACE VARCHAR2(128)       NOT NULL,
    PAYLOAD_TYPE_NAME      VARCHAR2(128)       NOT NULL,
    PAYLOAD_TYPE_REVISION  VARCHAR2(64),
    PAYLOAD                BINARY(4096),
    INSERTED               TIMESTAMP           NOT NULL,
    TTL_DURATION           VARCHAR2(32),
    EXPIRATION             TIMESTAMP,
    RETRIES                INTEGER             NOT NULL,
    NEXT_RETRY             TIMESTAMP,
    ERROR                  CLOB(10000)
);
```