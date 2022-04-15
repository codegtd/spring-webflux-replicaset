#!/bin/bash
echo "================================================================================================"
echo                    "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Started..."
echo "================================================================================================"
#MONGODB1=`ping -c 1 mongo1 | head -1  | cut -d "(" -f 2 | cut -d ")" -f 1`
#MONGODB2=`ping -c 1 mongo2 | head -1  | cut -d "(" -f 2 | cut -d ")" -f 1`
#MONGODB3=`ping -c 1 mongo3 | head -1  | cut -d "(" -f 2 | cut -d ")" -f 1`

MONGODB1=mongo1
MONGODB2=mongo2
MONGODB3=mongo3
MONGOPORT=9042

echo "**********************************************" ${MONGODB1}
echo "Waiting for startup.."
until curl http://${MONGODB1}:${MONGOPORT}/serverStatus\?text\=1 2>&1 | grep uptime | head -1; do
  printf '.'
  sleep 5
#  read && echo "This is a test"
done

# echo curl http://${MONGODB1}:28017/serverStatus\?text\=1 2>&1 | grep uptime | head -1
# echo "Started.."

echo SETUP.sh time now: `date +"%T" `
mongo --host ${MONGODB1}:${MONGOPORT} <<EOF
var cfg = {
    "_id": "docker-rs",
    "protocolVersion": 1,
    "version": 1,
    "members": [
        {
            "_id": 0,
            "host": "${MONGODB1}:${MONGOPORT}",
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
    ],settings: {chainingAllowed: true}
};
rs.initiate(cfg, { force: true });
rs.reconfig(cfg, { force: true });
rs.slaveOk();
db.getMongo().setReadPref('nearest');
db.getMongo().setSlaveOk();
rs.status();
db.createUser({
    user: "xxxx",
    pwd: "admin",
    roles: [{role: "root", db: "admin"},"root"]
  });
rs.status();
EOF
echo "================================================================================================"
echo                    "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Done"
echo "================================================================================================"