#!/usr/bin/env bash

DIR=$(dirname "$0")
source "$DIR/env.env"

CLIENT_BIN=kcat
JSON=$(jq . "$DIR/local/book-flight.json")

echo "$JSON" | "$CLIENT_BIN" \
  -b "$KAFKA_BOOTSTRAP_SERVER_HOST:$KAFKA_BOOTSTRAP_SERVER_PORT" \
  -t "$KAFKA_TOPIC_CORRELATE_FLIGHTS" -P

echo "====================================================================="
echo "Waiting for response..."
echo "====================================================================="

"$CLIENT_BIN" -C -b "$KAFKA_BOOTSTRAP_SERVER_HOST:$KAFKA_BOOTSTRAP_SERVER_PORT" \
  -t "$KAFKA_TOPIC_CORRELATE_FLIGHTS_RESULT" -C

