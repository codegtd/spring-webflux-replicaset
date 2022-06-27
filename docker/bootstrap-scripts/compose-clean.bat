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
docker-compose -f singlenode-replicaset-noauth-compose.yml down --remove-orphans

cd ..\rs-singlenode-auth
cd
docker-compose -f singlenode-replicaset-auth-compose.yml down --remove-orphans

::cd ..\rs-threenodes
::cd
::docker-compose -f threenodes-replicaset-noauth-compose.yml down --remove-orphans

cd ..\mongo-secrets
cd
docker-compose -f secrets-compose.yml down --remove-orphans


cd ..\mongo-standalone
cd
docker-compose -f compose-dev-standalone.yml down --remove-orphans
cd ..

::------------------------------------------------------------------------------
docker container prune --force
docker system prune --volumes --force
docker network prune --force
docker builder prune --all --force
::------------------------------------------------------------------------------
docker image rm pauloportfolio/apithreenodes
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