#!/bin/bash


echo "================================================================================================"
echo            "BOOTSTRAP-SCRIPT: Create User Admin - Started..1."
echo "================================================================================================"
echo "$MONGO_INITDB_DATABASE"
echo "$MONGO_INITDB_ROOT_USERNAME"
echo "${MONGO_INITDB_ROOT_PASSWORD}"
SLEEPTIME=5s
echo "================================================================================================"
echo            "BOOTSTRAP-SCRIPT: Create User Admin - Started.2.."
echo "================================================================================================"
sleep ${SLEEPTIME}

echo "================================================================================================"
echo            "BOOTSTRAP-SCRIPT: Create User Admin - Started.3.."
echo "================================================================================================"

mongo --authenticationDatabase root_admin "$MONGO_INITDB_DATABASE" <<EOF
    db.createUser({
        user: '$MONGO_INITDB_USERNAME',
        pwd: '$MONGO_INITDB_PASSWORD',
        roles: [ { role: 'readWrite', db: '$MONGO_INITDB_DATABASE' } ]
    })
EOF
sleep 10s

echo "================================================================================================"
echo            "BOOTSTRAP-SCRIPT: Create KeyFile - Started..."
echo "================================================================================================"
openssl rand -base64 756 > /auth/file.key
chmod 400 /auth/file.key
chown 999:999 file.key
ls /auth/

echo "================================================================================================"
echo            "BOOTSTRAP-SCRIPT: Finished..."
echo "================================================================================================"
sleep 10s