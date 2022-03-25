create table COR_MESSAGE(
  ID varchar2(64) unique not null,
  PAYLOAD_ENCODING varchar2(64) not null,
  PAYLOAD_TYPE_NAMESPACE varchar2(128) not null,
  PAYLOAD_TYPE_NAME varchar2(128) not null,
  PAYLOAD_TYPE_REVISION varchar2(64),
  PAYLOAD binary(1024),
  INSERTED timestamp not null,
  TTL_DURATION varchar2(32),
  EXPIRATION timestamp,
  RETRIES integer not null,
  NEXT_RETRY timestamp,
  ERROR clob(10000)
);
