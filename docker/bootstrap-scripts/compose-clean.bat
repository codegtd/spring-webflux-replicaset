@echo off
echo ===========================================================================
echo                           CLEAN-UP: starting
echo ===========================================================================
cd
cd ..
docker system df

echo ===========================================================================
echo           CLEAN-UP: compose orphans %parameter1%
echo ===========================================================================
cd rs-singlenode
docker-compose -f dev-singlenode-replicaset-noauth-compose.yml down --remove-orphans

cd ..
cd rs-singlenode-auth
docker-compose -f dev-singlenode-replicaset-auth-compose.yml down --remove-orphans

cd ..
cd rs-threenodes
docker-compose -f dev-threenodes-replicaset-noauth-compose.yml down --remove-orphans
::------------------------------------------------------------------------------
docker container prune --force
docker system prune --volumes --force
docker network prune --force
docker builder prune --all --force

::----------------------------IMAGES TO CHANGE----------------------------------
docker image rm pauloportfolio/api3nodes
docker image rm pauloportfolio/api3nodesauth
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
echo                         CLEAN-UP: finishing
echo ===========================================================================