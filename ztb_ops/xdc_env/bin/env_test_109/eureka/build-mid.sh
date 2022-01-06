#!/bin/sh

sed -i 's/\r//' ../common/config.sh
source ../common/config.sh

tunnel_to_path="/root/ztb_ops/sd_env/env_dev_109/image/image_service/"

docker pull 10.20.54.143:8000/ztb-sd/eureka:latest

echo ------------ starting tag service ------------
docker tag 10.20.54.143:8000/ztb-sd/eureka:latest regcenter:latest

echo ------------ save tar.gz ------------
docker save regcenter:latest > ../../../image/image_service/regcenter.tar.gz

echo ------------ rmi service ------------
docker rmi regcenter:latest

echo "------------Start copy to sd_env------------"
sshpass -p $tunnel_to_password \
ssh $tunnel_to_username@$tunnel_to_ip "mkdir -p $tunnel_to_path"
sshpass -p $tunnel_to_password \
scp -r ../../../image/image_service/regcenter.tar.gz $tunnel_to_username@$tunnel_to_ip:$tunnel_to_path

sshpass -p $tunnel_to_password \
ssh $tunnel_to_username@$tunnel_to_ip ls -ltr $tunnel_to_path

echo "------------Finish copy to sd_env------------"

echo ------------ finish build service and copy to sd-env ------------
