@echo off
set parameter1=%1

echo ===========================================================================
echo           1) DOCKER-COMPOSE: Starting in profile %parameter1%
echo ===========================================================================
cd
cd ..\..

echo ===========================================================================
echo                     2) DOCKER-COMPOSE: Maven Procedures
echo ===========================================================================
call mvn clean package -DskipTests

echo ===========================================================================
echo               3.1) DOCKER-COMPOSE: Cleaning previous services
echo ===========================================================================
cd
cd docker\bootstrap-scripts
call compose-clean.bat %parameter1%

echo ===========================================================================
echo       4) DOCKER-COMPOSE: Uping the Compose-Service(s): %parameter1%
echo ===========================================================================
cd

cd rs-singlenode
if %parameter1%==devsingle (docker-compose -f dev-singlenode-replicaset-noauth-compose.yml --verbose up --build --force-recreate)
cd ..

cd rs-singlenode-auth
if %parameter1%==prodsingleauth (docker-compose -f prod-singlenode-replicaset-auth-compose.yml up --build --force-recreate)
cd ..

cd rs-threenodes
if %parameter1%==devthree (docker-compose -f dev-threenodes-replicaset-noauth-compose.yml up --build --force-recreate)
cd ..

cd mongo-secrets
if %parameter1%==mongosecrets (docker-compose -f secrets-compose.yml up --build --force-recreate)
cd ..

echo ===========================================================================
echo           5) DOCKER-COMPOSE: Finishing in profile %parameter1%
echo ===========================================================================
::if %parameter1%==devstd (docker-compose -f compose-dev-standalone.yml --verbose up --build --force-recreate)