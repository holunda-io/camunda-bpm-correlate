#!/usr/bin/env bash

source env.env

CLIENT_BIN="kafkacat"

case "$1" in
  "create-reservation")
  JSON="$(jq . "local/create-reservation.json")"
  echo Sending create reservation message: $JSON
  echo $JSON | $CLIENT_BIN \
    -b $KAFKA_BOOTSTRAP_SERVER_HOST:$KAFKA_BOOTSTRAP_SERVER_PORT \
    -t $KAFKA_TOPIC_CORRELATE_INGRES \
    -P \
    -H X-CORRELATE-ID=1647609548 \
    -H X-CORRELATE-PayloadType-FQCN=io.holunda.camunda.bpm.example.kafka.domain.ReservationReceivedEvent
  ;;
  *)
  echo "Usage: $0 create-reservation"
  exit 1
  ;;
esac
