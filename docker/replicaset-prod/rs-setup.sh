#!/bin/bash
echo "-----------------------------------------------------------------------------------------------"
echo                    "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Started..."
echo "-----------------------------------------------------------------------------------------------"

MONGODB1=mongo1
MONGODB2=mongo2
MONGODB3=mongo3
MONGO_PORT=9042
MONGO_RS=docker-rs

echo "-----------------------------------------------------------------------------------------------"
echo      "SCRIPT: MONGO-DB REPLICASET - CONFIGURING REPLICASET:" ${MONGODB1}
echo                           SETUP.sh time now: `date +"%T" `
echo "-----------------------------------------------------------------------------------------------"
#echo SETUP.sh time now: `date +"%T" `
mongo --host ${MONGODB1}:${MONGO_PORT} <<EOF
var cfg = {
    "_id": "${MONGO_RS}",
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
            "priority": 0
        }
    ]
};
rs.initiate(cfg, { force: true });
rs.secondaryOk();
db.getMongo().setReadPref('primary');
rs.status();
EOF
echo "-----------------------------------------------------------------------------------------------"
echo                    "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Done"
echo "-----------------------------------------------------------------------------------------------"