#!/usr/bin/env bash


DIR=$(dirname "$0")
source "$DIR/reservation-kafka/env.env"
RESERVATION_ID=$(cat /proc/sys/kernel/random/uuid)

CLIENT_BIN=kcat
TIMESTAMP=$(date +%s%N)

case "$1" in
  "reservation")
    JSON=$(jq ".reservationId =\""$RESERVATION_ID\"" "$DIR/../common/local/reservation-created.json")
    EVENT_TYPE=io.holunda.camunda.bpm.example.common.domain.ReservationReceivedEvent
    echo "Sending create reservation message: $JSON"
  ;;

  "flight")
    JSON=$(jq . "$DIR/../common/local/flight-reserved.json")
    EVENT_TYPE=io.holunda.camunda.bpm.example.common.domain.flight.FlightReservationConfirmedEvent
    echo "Sending create flight message: $JSON"
  ;;

  "hotel")
    JSON=$(jq . "$DIR/../common/hotel-booked.json")
    EVENT_TYPE=io.holunda.camunda.bpm.example.common.domain.hotel.HotelReservationConfirmedEvent
    echo "Sending create hotel message: $JSON"
  ;;
  "show")
    "$CLIENT_BIN" -b "$KAFKA_BOOTSTRAP_SERVER_HOST:$KAFKA_BOOTSTRAP_SERVER_PORT" \
    -t "$KAFKA_TOPIC_CORRELATE_INGRESS" \
    -C
    exit 0
  ;;
  "all")
    $0 reservation
    $0 flight
    $0 hotel
  ;;
  *)
    echo "Usage: $0 reservation | flight | hotel"
    exit 1
  ;;
esac

echo "$JSON" | "$CLIENT_BIN" \
  -b "$KAFKA_BOOTSTRAP_SERVER_HOST:$KAFKA_BOOTSTRAP_SERVER_PORT" \
  -t "$KAFKA_TOPIC_CORRELATE_INGRESS" \
  -P \
  -H "X-CORRELATE-PayloadType-FQCN=$EVENT_TYPE"
