@echo off
echo ===========================================================================
echo                           CLEAN-UP: starting
echo ===========================================================================
cd
docker system df

echo ===========================================================================
echo           CLEAN-UP: compose orphans %parameter1% %parameter2%
echo ===========================================================================
docker-compose -f compose-dev-replicaset.yml  down --remove-orphans
docker-compose -f compose-dev-standalone.yml  down --remove-orphans
docker-compose -f compose-test-standalone.yml  down --remove-orphans
docker-compose -f compose-prod-replicaset-auth.yml down --remove-orphans
docker-compose -f compose-prod-replicaset-noauth.yml down --remove-orphans
::------------------------------------------------------------------------------
docker container prune --force
docker system prune --volumes --force
docker network prune --force
docker builder prune --all --force

::----------------------------IMAGES TO CHANGE----------------------------------
::docker image rm pauloportfolio/mongo1
::docker image rm pauloportfolio/api

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
echo                         CLEAN-UP: finishing
echo ===========================================================================