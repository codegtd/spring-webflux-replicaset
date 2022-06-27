#! /bin/bash

MONGODB1=mongo1
MONGODB2=mongo2
MONGODB3=mongo3
DELAY_STARTING=$1
MONGO_PORT=$2

echo "================================================================================================"
echo            "RS_SETUP-SCRIPT: Configure Replicaset - Started..." `date +"%T" `
echo "================================================================================================"
sleep 40s
echo "DELAY DONE"


echo ${MONGODB1}
echo "$MONGO_INITDB_ROOT_USERNAME"
echo "${MONGO_INITDB_ROOT_PASSWORD}"
echo ${MONGO_INITDB_ROOT_PASSWORD}
echo ${MONGO_REPLICA_SET_NAME}
echo ${MONGODB3}
echo "$DELAY_STARTING"
echo "$MONGO_PORT"

mongo --host ${MONGODB1}:"${MONGO_PORT}" <<EOF
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
            "priority": 1
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
echo            "RS_SETUP-SCRIPT: Configure Replicaset - Finished..." `date +"%T" `
echo "================================================================================================"