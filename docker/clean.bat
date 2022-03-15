@echo off
echo ===========================================================================
echo                           CLEAN-UP: starting
echo ===========================================================================

cd
docker system df

echo ===========================================================================
echo           CLEAN-UP: compose cleaning-up %parameter1% %parameter2%
echo ===========================================================================
docker-compose -f compose-standalone.yml down --remove-orphans
docker-compose -f compose-rs-node1.yml down --remove-orphans
docker-compose -f compose-rs-node3.yml down --remove-orphans
::------------------------------------------------------------------------------
docker container prune --force
docker system prune --volumes --force
docker network prune --force
docker builder prune --all --force

::----------------------------IMAGES TO CHANGE----------------------------------
docker image rm pauloportfolio/mongo1

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