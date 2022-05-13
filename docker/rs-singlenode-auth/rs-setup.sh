# !/bin/bash

echo "================================================================================================"
echo                               "RS-SCRIPT: Starting..."
echo "================================================================================================"

echo "================================================================================================"
echo     "RS-SCRIPT: REPEAT Env-Vars FROM: env-rs-root-auth.env + env-rs-general.env or Secrets "
echo "================================================================================================"
MONGO_DATABASE=admin33
MONGO_INITDB_ROOT_USERNAME=root
MONGO_INITDB_ROOT_PASSWORD=password
HOST_DB_CONTAINER_NAME=mongo1-db
PORT_DB=27017
SLEEP_TIME=5s

echo "VAR 1: $MONGO_DATABASE"
echo "VAR 2: $MONGO_INITDB_ROOT_USERNAME"
echo "VAR 3: ${MONGO_INITDB_ROOT_PASSWORD}"
echo "VAR 4: ${HOST_DB_CONTAINER_NAME}"
echo "VAR 5: ${PORT_DB}"

echo "================================================================================================"
echo                              "RS-SCRIPT: Configuring Replicaset"
echo "================================================================================================"
sleep $SLEEP_TIME
mongo -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" --host $HOST_DB_CONTAINER_NAME --port $PORT_DB --eval "rs.initiate()"
sleep $SLEEP_TIME
mongo -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" --host $HOST_DB_CONTAINER_NAME --port $PORT_DB --eval "rs.status()"
sleep $SLEEP_TIME
mongo -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" --host $HOST_DB_CONTAINER_NAME --port $PORT_DB --eval "rs.conf()"
sleep $SLEEP_TIME

echo "================================================================================================"
echo                         "RS-SCRIPT: Cleaning Environment variables..."
echo "================================================================================================"
unset MONGO_DATABASE
unset MONGO_INITDB_ROOT_USERNAME
unset MONGO_INITDB_ROOT_PASSWORD
unset HOST_DB_CONTAINER_NAME
unset PORT_DB

echo "VAR 1: $MONGO_DATABASE"
echo "VAR 2: $MONGO_INITDB_ROOT_USERNAME"
echo "VAR 3: ${MONGO_INITDB_ROOT_PASSWORD}"
echo "VAR 4: ${HOST_DB_CONTAINER_NAME}"
echo "VAR 5: ${PORT_DB}"

echo "================================================================================================"
echo                                    "RS-SCRIPT: Finished..."
echo "================================================================================================"