#!/bin/sh

scriptPath="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
imageService=$scriptPath"/image/image_service"
imageWeb=$scriptPath"/image/image_product_118"
dockerComposePath=$scriptPath"/docker-compose/ztb"

echo "cd ${dockerComposePath}...."
cd ${dockerComposePath}

echo "stoping containers...."
docker-compose down -v

echo "delete docker images...."
docker rmi scheduling-service:latest
docker rmi workflow-service:latest
docker rmi permission-service:latest
docker rmi vendor-perm-service:latest
docker rmi sourcing-service:latest
docker rmi vendor-service:latest
docker rmi ztb-web-sd:latest
docker rmi ztb-portal-sd:latest

echo "docker images...."
docker images

echo "load docker images...."
docker load <${imageService}/scheduling-service.tar.gz
docker load <${imageService}/workflow-service.tar.gz
docker load <${imageService}/permission-service.tar.gz
docker load <${imageService}/vendor-perm-service.tar.gz
docker load <${imageService}/sourcing-service.tar.gz
docker load <${imageService}/vendor-service.tar.gz
docker load <${imageWeb}/ztb-web-sd.tar.gz
docker load <${imageWeb}/ztb-portal-sd.tar.gz

echo "docker images...."
docker images

echo "starting containers...."
docker-compose down -v && docker-compose up -d
sleep 5
docker ps