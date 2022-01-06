#!/bin/sh

scriptPath="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
imageService=$scriptPath"/image/image_service"
imageWeb=$scriptPath"/image/image_dev_109"
dockerComposePath=$scriptPath"/docker-compose/midware"

echo "cd ${dockerComposePath}...."
cd ${dockerComposePath}

echo "stoping containers...."
docker-compose down -v

echo "delete docker images...."
docker rmi mid-regcenter:latest
docker rmi workflow-service:latest

echo "docker images...."
docker images

echo "load docker images...."
docker load <${imageService}/regcenter.tar.gz
docker load <${imageService}/mid-workflow.tar.gz

echo "docker images...."
docker images

echo "starting containers...."
docker-compose down -v && docker-compose up -d
sleep 5
docker ps
