#!/usr/bin/env bash
# =============================================================================
# VehicleSphere — Custom Nagios Check Plugin for Spring Boot Actuator Health
# Exit Codes: 0 = OK, 1 = WARNING, 2 = CRITICAL, 3 = UNKNOWN
# =============================================================================

HOST="127.0.0.1"
PORT="8080"
ENDPOINT="/actuator/health"

while getopts "H:p:e:" opt; do
  case $opt in
    H) HOST="$OPTARG" ;;
    p) PORT="$OPTARG" ;;
    e) ENDPOINT="$OPTARG" ;;
    *) echo "Usage: $0 -H <host> -p <port> -e <endpoint>"; exit 3 ;;
  esac
done

URL="http://${HOST}:${PORT}${ENDPOINT}"

RESPONSE=$(curl -s -m 5 "${URL}")
CURL_STATUS=$?

if [ $CURL_STATUS -ne 0 ]; then
  echo "CRITICAL - Unable to reach VehicleSphere Actuator at ${URL} (curl error ${CURL_STATUS})"
  exit 2
fi

STATUS=$(echo "${RESPONSE}" | grep -o '"status":"[^"]*"' | head -1 | cut -d':' -f2 | tr -d '"')

if [ "${STATUS}" = "UP" ]; then
  echo "OK - VehicleSphere Spring Boot Backend is UP and Healthy | ${RESPONSE}"
  exit 0
elif [ "${STATUS}" = "DOWN" ]; then
  echo "CRITICAL - VehicleSphere Spring Boot Backend reports DOWN | ${RESPONSE}"
  exit 2
elif [ -n "${STATUS}" ]; then
  echo "WARNING - VehicleSphere Spring Boot Backend status is ${STATUS} | ${RESPONSE}"
  exit 1
else
  echo "UNKNOWN - Unparseable health response from ${URL}: ${RESPONSE}"
  exit 3
fi
