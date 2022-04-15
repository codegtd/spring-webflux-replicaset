#!/bin/bash
echo "================================================================================================"
echo                    "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Started..."
echo "================================================================================================"

mongo --port 9042 <<EOF
var config = {
    "_id": "docker-rs",
    "version": 1,
    "members": [
        {
            "_id": 1,
            "host": "mongo1:9042",
            "priority": 3
        },
        {
            "_id": 2,
            "host": "mongo2:9142",
            "priority": 2
        },
        {
            "_id": 3,
            "host": "mongo3:9242",
            "priority": 1
        }
    ]
};
rs.initiate(config, { force: true });
rs.status();
use admin;
db.createUser({user: "admin",pwd: "admin",roles: [ { role: "root", db: "admin" }, "root" ]});
exit
EOF
"127.0.0.1 localhost mongo1 mongo2 mongo3" >>/etc/hosts;
cat > /etc/hosts;
sleep 5;
echo "================================================================================================"
echo                      "SCRIPT: MONGO-DB REPLICASET - CONFIGURATION: Done"
echo "================================================================================================"
#---------------------------------------------------
#sleep 5 &&
#mongo --host mongo1_primary --port 9042 --eval \"rs.initiate(config)\" &&
#sleep 2 &&
#mongo --host mongo1_primary --port 9042 --eval \"rs.status()\" &&
#sleep infinity"