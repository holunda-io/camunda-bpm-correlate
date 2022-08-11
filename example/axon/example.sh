#!/usr/bin/env bash

DIR="$(dirname $0)"

CLIENT_BIN="curl"
TIMESTAMP=$(date +%s%N)
RESERVATION_ID=$(cat /proc/sys/kernel/random/uuid)

case "$1" in
  "reservation")
    JSON=$(jq ".reservationId =\"$RESERVATION_ID\"" "$DIR/../common/local/reservation-created.json")
    echo "Sending create reservation message: $JSON"
  ;;

  *)
    echo "Usage: $0 reservation"
    exit 1
  ;;
esac

$CLIENT_BIN \
  -X POST localhost:8080/process/reservation \
  -H "Content-Type: application/json" \
  -d "$JSON"
