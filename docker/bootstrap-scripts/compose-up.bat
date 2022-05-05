@echo off
echo ===========================================================================
echo                        1) DOCKER-COMPOSE: Starts...
echo ===========================================================================

echo ===========================================================================
echo                     2) DOCKER-COMPOSE: Maven Procedures
echo ===========================================================================
cd
cd ..\..
call mvn clean package -DskipTests

echo ===========================================================================
echo               3.1) DOCKER-COMPOSE: Cleaning previous services
echo ===========================================================================
cd
cd docker
cd bootstrap-scripts
call compose-clean.bat

set parameter1=%1
echo ===========================================================================
echo       4) DOCKER-COMPOSE: Uping the Compose-Service(s): %parameter1%
echo ===========================================================================
cd
cd ..

cd rs-singlenode
if %parameter1%==devsingle (docker-compose -f dev-singlenode-replicaset-noauth-compose.yml --verbose up --build --force-recreate)
cd ..

cd rs-singlenode-auth
if %parameter1%==prodsingleauth (docker-compose -f prod-singlenode-replicaset-auth-compose.yml up --build --force-recreate)
cd ..

cd rs-threenodes
if %parameter1%==devthree (docker-compose -f dev-threenodes-replicaset-noauth-compose.yml up --build --force-recreate)
cd ..

echo ===========================================================================
echo                     5) DOCKER-COMPOSE: ...Ending
echo ===========================================================================
::if %parameter1%==devstd (docker-compose -f compose-dev-standalone.yml --verbose up --build --force-recreate)