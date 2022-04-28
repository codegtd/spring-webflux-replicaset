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
cd ..\docker
if %parameter1%==devrs  (docker-compose -f dev-singlenode-replicaset-noauth-compose.yml --verbose up --build --force-recreate)
if %parameter1%==prodrsn (docker-compose -f dev-threenodes-replicaset-noauth-compose.yml up --build --force-recreate)
if %parameter1%==prodrsa (docker-compose -f compose-prod-replicaset-auth.yml up --build --force-recreate)

echo ===========================================================================
echo                     5) DOCKER-COMPOSE: ...Ending
echo ===========================================================================
docker-compose ls
docker container ls
::if %parameter1%==devstd (docker-compose -f compose-dev-standalone.yml --verbose up --build --force-recreate)