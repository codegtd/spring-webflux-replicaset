#!/bin/bash
echo "-----------------------------------------------------------------------------------------------"
echo                    "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Started..."
echo "-----------------------------------------------------------------------------------------------"
#MONGODB1=`ping -c 1 mongo1 | head -1  | cut -d "(" -f 2 | cut -d ")" -f 1`
#MONGODB2=`ping -c 1 mongo2 | head -1  | cut -d "(" -f 2 | cut -d ")" -f 1`
#MONGODB3=`ping -c 1 mongo3 | head -1  | cut -d "(" -f 2 | cut -d ")" -f 1`

MONGODB1=mongo1
MONGODB2=mongo2
MONGODB3=mongo3
MONGOPORT=9042
MONGO_RS=docker-rs

echo "-----------------------------------------------------------------------------------------------"
echo      "SCRIPT: MONGO-DB REPLICASET - CONFIGURING REPLICASET:" ${MONGODB1}
echo                           SETUP.sh time now: `date +"%T" `
echo "-----------------------------------------------------------------------------------------------"
#echo SETUP.sh time now: `date +"%T" `
mongo --host ${MONGODB1}:${MONGOPORT} <<EOF
var cfg = {
    "_id": "${MONGO_RS}",
    "version": 1,
    "members": [
        {
            "_id": 0,
            "host": "${MONGODB1}:${MONGOPORT}",
            "priority": 3
        },
        {
            "_id": 1,
            "host": "${MONGODB2}:9142",
            "priority": 2
        },
        {
            "_id": 2,
            "host": "${MONGODB3}:9242",
            "priority": 1
        }
    ]
};
rs.initiate(cfg, { force: true });
rs.reconfig(cfg, { force: true });
rs.slaveOk();
db.getMongo().setReadPref('nearest');
db.getMongo().setSlaveOk();
rs.status();
use admin;
db.createUser({
    user: "admin",
    pwd: "xxxx",
    roles: [{role: "root", db: "admin"},"root"]
  });
db.auth("admin","xxxx");
EOF
#ls;
echo "-----------------------------------------------------------------------------------------------"
echo                    "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Done"
echo "-----------------------------------------------------------------------------------------------"