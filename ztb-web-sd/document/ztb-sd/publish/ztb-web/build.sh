#!/bin/sh

fromContainer="ztb-web-mid"
toContainer="ztb-web-sd"
scriptPath="/home/rd/apps/ztb-sd/publish/ztb-web"

echo ------------Building ${fromContainer}------------
cd ${scriptPath} && rm -rf dist
docker-compose down -v && docker-compose up -d
while true; do
  currTime=$(date "+%Y-%m-%d %H:%M:%S")
  exist=`docker inspect --format '{{.State.Running}}' ${fromContainer}`
  if [ "${exist}" != "true" ]; then
    docker start ${fromContainer}
    echo "docker is starting"
  else
    echo "docker inside process is starting at "${currTime}
    docker cp ${fromContainer}:/usr/local/nginx/html/. ./dist/
    count=`ls dist/|wc -w`
    if [ "$count" -ge 3 ]; then
      docker-compose down -v
      echo "docker cp ${fromContainer}:/usr/local/nginx/html/. ./dist/"
      ls dist/
      break
    fi
  fi
  sleep 0.5
done

echo ------------Building ${toContainer}------------
docker stop ${toContainer} && docker rm ${toContainer} \
&& docker rmi ${toContainer} \
&& docker-compose -f docker-compose2.yml up -d --build
while true; do
  exist2=`docker inspect --format '{{.State.Running}}' ${toContainer}`
  if [ "${exist2}" != "true" ]; then
    docker-compose -f docker-compose2.yml up -d
    echo "docker is starting"
  else
    echo "docker exist, and tar to /root/${toContainer}.tar.gz"
    docker save ${toContainer} > /root/${toContainer}.tar.gz &
    break
  fi
  sleep 1
done
rm -rf ./dist
docker stop ${toContainer} && docker rm ${toContainer}
docker ps
