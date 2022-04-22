@echo off
echo ===========================================================================
echo                        1) DOCKER-COMPOSE: Starts...
echo ===========================================================================

echo ===========================================================================
echo                     2) DOCKER-COMPOSE: Maven Procedures
echo ===========================================================================
cd ..
call mvn clean package -DskipTests

echo ===========================================================================
echo               3.1) DOCKER-COMPOSE: Cleaning previous services
echo ===========================================================================
cd
cd docker
call compose-clean.bat


set parameter1=%1
echo ===========================================================================
echo       4) DOCKER-COMPOSE: Uping the Compose-Service(s): %parameter1%
echo ===========================================================================
if %parameter1%==devrs  (docker-compose -f compose-dev-replicaset.yml --verbose  up --build --force-recreate --detach)
if %parameter1%==devstd (docker-compose -f compose-dev-standalone.yml --verbose  up --build --force-recreate --detach)
if %parameter1%==prodrs (docker-compose -f compose-prod-replicaset.yml up --build --force-recreate)
::if %parameter1%==prodrs (docker-compose -f compose-prod-replicaset.yml up --build --force-recreate)
if %parameter1%==test  (docker-compose -f compose-test-standalone.yml --verbose  up --build --force-recreate --detach)
if %parameter1%==test2  (docker-compose -f compose-test-standalone.yml up --build --force-recreate)

echo ===========================================================================
echo                     5) DOCKER-COMPOSE: ...Ending
echo ===========================================================================