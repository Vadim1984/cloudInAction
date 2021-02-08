############################# general info ###########################
local app    - http://localhost:3333/healthCheck
swagger page - http://<app domain>:<app port>/swagger-ui.html
############################# docker #################################
1. docker repo - https://hub.docker.com/repository/docker/shvadim84/cloudinactiondockerrepo
2. build and tag image : docker build -t shvadim84/cloudinactiondockerrepo .
3. push to repository  : docker push shvadim84/cloudinactiondockerrepo
4. pull image : docker pull shvadim84/cloudinactiondockerrepo
5. run container : docker run --name cloudinactioncontainer -d -p 80:8080  shvadim84/cloudinactiondockerrepo
############################# manage docker daemon on ec2 linux ###########
1. install docker daemon:
   sudo yum update -y
   sudo yum install docker -y
2. add ec2 user to the docker group to execute Docker commands without using sud
   sudo usermod -a -G docker ec2-user

3. sudo systemctl start docker
4. sudo systemctl stop docker
5. sudo systemctl status docker
############################# install cloudWatch agent ####################
https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/QuickStartEC2Instance.html
connect docker logs to cloudWatch - https://docs.docker.com/config/containers/logging/awslogs/

 - creating single container and connect log to cloudWatch
docker run --log-driver=awslogs --log-opt awslogs-group=/var/log/messages --log-opt awslogs-region=us-east-2 --name cloudinactioncontainer -d -p 80:8080  shvadim84/cloudinactiondockerrepo
###########################################################################



