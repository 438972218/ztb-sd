#!/bin/sh

sed -i 's/\r//' ../common/config.sh
source ../common/config.sh

tunnel_to_path="/root/ztb_ops/sd_env/env_test_142/image/image_test_142/"
fromImage="10.20.54.143:8000/ztb-sd/ztb-portal:latest"
fromContainer="ztb-portal"
toContainer="ztb-portal-sd"
imageFolder="image_test_142"

scriptPath="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
imagePath="$(cd "$scriptPath/../../../image/"$imageFolder && pwd)"
echo ------------image path: ${imagePath}------------

echo ------------Building ${fromContainer}------------

echo ------------ rmi docker image: ${toContainer} / ${fromImage} ------------
cd ${scriptPath} && rm -rf dist
docker-compose down -v
docker rmi ${toContainer} && docker rmi ${fromImage}
docker images|grep ${fromContainer}

echo ------------ pull docker image: ${fromImage} ------------
docker-compose up -d
docker images|grep ${fromContainer}
docker ps -a

while true; do
  currTime=$(date "+%Y-%m-%d %H:%M:%S")
  exist=$(docker inspect --format '{{.State.Running}}' ${fromContainer})
  if [ "${exist}" != "true" ]; then
    echo "------ docker is starting"
    docker-compose up -d
  else
    echo "------ docker inside process is starting at "${currTime}
    docker cp ${fromContainer}:/usr/local/nginx/html/. ./dist/
    count=$(ls dist/ | wc -w)
    if [ "$count" -ge 3 ]; then
      echo "------ docker cp ${fromContainer}:/usr/local/nginx/html/. ./dist/"
      sleep 3
      docker cp ${fromContainer}:/usr/local/nginx/html/. ./dist/
      docker-compose down -v
      ls dist/
      break
    fi
  fi
  sleep 0.5
done

echo ------------Building ${toContainer}------------
docker stop ${toContainer} && docker rm ${toContainer} && docker rmi ${toContainer}
docker ps -a
docker-compose -f docker-compose2.yml up -d --build
while true; do
  exist2=$(docker inspect --format '{{.State.Running}}' ${toContainer})
  if [ "${exist2}" != "true" ]; then
    docker-compose -f docker-compose2.yml up -d
    echo "------ docker is starting"
  else
    echo "------ docker ${toContainer} build and up success, tar to /root/${toContainer}.tar.gz"
    docker save ${toContainer} >${imagePath}/${toContainer}.tar.gz &
    break
  fi
  sleep 1
done
rm -rf ./dist
docker stop ${toContainer} && docker rm ${toContainer}
docker ps -a

echo "------------Start copy to sd_env------------"
sshpass -p $tunnel_to_password \
ssh $tunnel_to_username@$tunnel_to_ip "mkdir -p $tunnel_to_path"
sshpass -p $tunnel_to_password \
scp ${imagePath}/${toContainer}.tar.gz $tunnel_to_username@$tunnel_to_ip:$tunnel_to_path

sshpass -p $tunnel_to_password \
ssh $tunnel_to_username@$tunnel_to_ip ls -ltr $tunnel_to_path
echo "------------Finish copy to sd_env------------"