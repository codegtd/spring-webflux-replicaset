# !/bin/bash

echo "================================================================================================"
echo            "BOOTSTRAP-RS-SCRIPT: Starting..."
echo "================================================================================================"

echo "================================================================================================"
echo            "BOOTSTRAP-RS-SCRIPT: Loading Environment Variables File..."
echo "================================================================================================"
MONGO_USERNAME=root
MONGO_PASSWORD=password
MONGO_DATABASE=admin33
HOST_DB=mongo1-db
PORT_DB=27017
SLEEP_TIME=5s

echo "VAR 1: $MONGO_DATABASE"
echo "VAR 2: $MONGO_USERNAME"
echo "VAR 3: ${MONGO_PASSWORD}"
echo "VAR 4: ${HOST_DB}"
echo "VAR 5: ${PORT_DB}"

echo "================================================================================================"
echo            "BOOTSTRAP-RS-SCRIPT: Configuring Replicaset"
echo "================================================================================================"
sleep $SLEEP_TIME
mongo -u "$MONGO_USERNAME" -p "$MONGO_PASSWORD" --host $HOST_DB --port $PORT_DB --eval "rs.initiate()"
sleep $SLEEP_TIME
mongo -u "$MONGO_USERNAME" -p "$MONGO_PASSWORD" --host $HOST_DB --port $PORT_DB --eval "rs.status()"
sleep $SLEEP_TIME
mongo -u "$MONGO_USERNAME" -p "$MONGO_PASSWORD" --host $HOST_DB --port $PORT_DB --eval "rs.conf()"
sleep $SLEEP_TIME

echo "================================================================================================"
echo            "BOOTSTRAP-RS-SCRIPT: Cleaning Environment variables..."
echo "================================================================================================"
unset MONGO_DATABASE
unset MONGO_USERNAME
unset MONGO_PASSWORD
unset HOST_DB
unset PORT_DB

echo "VAR 1: $MONGO_DATABASE"
echo "VAR 2: $MONGO_USERNAME"
echo "VAR 3: ${MONGO_PASSWORD}"
echo "VAR 4: ${HOST_DB}"
echo "VAR 5: ${PORT_DB}"

echo "================================================================================================"
echo            "BOOTSTRAP-RS-SCRIPT: Finished..."
echo "================================================================================================"