@echo off
set parameter1=%1

::if "%parameter1%" EQU "" ( docker-compose down --remove-orphans )

echo ===========================================================================
echo              CLEAN-UP: Starting in profile %parameter1%
echo ===========================================================================
cd
docker system df

echo ===========================================================================
echo                 CLEAN-UP: compose orphans %parameter1%
echo ===========================================================================
cd ..\rs-singlenode
cd
docker-compose -f dev-singlenode-replicaset-noauth-compose.yml down --remove-orphans
::if %parameter1%==devsingle (docker-compose -f dev-singlenode-replicaset-noauth-compose.ymldown --remove-orphans)
::cd ..

cd ..\rs-singlenode-auth
cd
::if %parameter1%==prodsingleauth (docker-compose -f dev-singlenode-replicaset-noauth-compose.yml down --remove-orphans)
docker-compose -f prod-singlenode-replicaset-auth-compose.yml down --remove-orphans

cd ..\rs-threenodes
cd
::if %parameter1%==devthree (docker-compose -f dev-singlenode-replicaset-noauth-compose.yml down --remove-orphans)
docker-compose -f dev-threenodes-replicaset-noauth-compose.yml down --remove-orphans

cd ..\mongo-secrets
cd
::if %parameter1%==mongosecrets (docker-compose -f mongo-secrets-compose.yml down --remove-orphans)
docker-compose -f secrets-compose.yml down --remove-orphans
cd ..

::------------------------------------------------------------------------------
docker container prune --force
docker system prune --volumes --force
docker network prune --force
docker builder prune --all --force
::------------------------------------------------------------------------------
docker image rm pauloportfolio/api3nodes
docker image rm pauloportfolio/apisinglenodeauth
docker image rm pauloportfolio/apisinglenode
::------------------------------------------------------------------------------

echo ===========================================================================
echo                      CLEAN-UP: listing images
echo ===========================================================================
docker image ls

echo ===========================================================================
echo                      CLEAN-UP: listing system
echo ===========================================================================
docker system df

echo ===========================================================================
echo               CLEAN-UP: Finishing in profile - %parameter1%
echo ===========================================================================