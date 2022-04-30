#!/bin/bash

MONGODB1=mongo1
MONGODB2=mongo2
MONGODB3=mongo3

echo "================================================================================================"
echo            "RS_SETUP-SCRIPT: Configure Replicaset - Started..."
echo "================================================================================================"
echo "**********************************************" ${MONGODB1}
echo "Waiting for startup.."
sleep 10s
echo "done"

echo SETUP.sh time now: `date +"%T" `

echo ${MONGODB1}
MONGO_PORT=9042
echo "$MONGO_INITDB_ROOT_USERNAME"
echo "${MONGO_INITDB_ROOT_PASSWORD}"
echo ${MONGO_INITDB_ROOT_PASSWORD}
echo ${MONGO_REPLICA_SET_NAME}
echo ${MONGODB3}

mongo --host ${MONGODB1}:${MONGO_PORT} -u "${MONGO_INITDB_ROOT_USERNAME}" -p "${MONGO_INITDB_ROOT_PASSWORD}" <<EOF
var cfg = {
    "_id": "${MONGO_REPLICA_SET_NAME}",
    "protocolVersion": 1,
    "version": 1,
    "members": [
        {
            "_id": 0,
            "host": "${MONGODB1}:${MONGO_PORT}",
            "priority": 2
        },
        {
            "_id": 1,
            "host": "${MONGODB2}:9142",
            "priority": 0
        },
        {
            "_id": 2,
            "host": "${MONGODB3}:9242",
            "priority": 0,
        }
    ]
};
rs.initiate(cfg, { force: true });
rs.secondaryOk();
db.getMongo().setReadPref('primary');
rs.status();
EOF
echo "================================================================================================"
echo            "RS_SETUP-SCRIPT: Configure Replicaset - Finished..."
echo "================================================================================================"
sleep 10s