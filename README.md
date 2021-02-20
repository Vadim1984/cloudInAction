############################# general info ###########################
local app    - http://localhost:3333/healthCheck
swagger page - http://<app domain>:<app port>/swagger-ui.html
############################# docker #################################
1. docker repo - https://hub.docker.com/repository/docker/shvadim84/cloudinactiondockerrepo
2. build and tag image : docker build -t shvadim84/cloudinactiondockerrepo:latest .
3. push to repository  : docker push shvadim84/cloudinactiondockerrepo
4. pull image : docker pull shvadim84/cloudinactiondockerrepo
5. run container : docker run -d -p 80:8080  shvadim84/cloudinactiondockerrepo
############################# manage docker daemon on ec2 linux ###########
1. install docker daemon:
   sudo yum update -y
   sudo yum install docker -y
2. add ec2 user to the docker group to execute Docker commands without using sud
   sudo usermod -a -G docker ec2-user

3. sudo systemctl start docker
4. sudo systemctl stop docker
5. sudo systemctl status docker

To automatically start Docker and Containerd on boot for other distros:
 sudo systemctl enable docker.service
 sudo systemctl enable containerd.service

To disable this behavior, use disable instead.

 sudo systemctl disable docker.service
 sudo systemctl disable containerd.service
############################# install cloudWatch agent ####################
https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/QuickStartEC2Instance.html
connect docker logs to cloudWatch - https://docs.docker.com/config/containers/logging/awslogs/

 - creating single container and connect log to cloudWatch
docker run --log-driver=awslogs --log-opt awslogs-group=/var/log/messages --log-opt awslogs-region=us-east-2 --name cloudinactioncontainer -d -p 80:8080  shvadim84/cloudinactiondockerrepo
###########################################################################
docker run -d -p 8081:8080 -p 50000:50000 jenkins/jenkins:lts
docker exec -it "container" bash
security key - cat /var/jenkins_home/secrets/initialAdminPassword

-- docker exec -it -u root "container" bash
-- install docker inside jenkins container
1. check os: cat /etc/os-release
2. install depends on OS : https://docs.docker.com/engine/install/
3. add jenkins user to docker group : usermod -aG docker jenkins

OR
1. bind socket to outside docker daemon from jenkins container:
    docker run -u 0 --privileged --name jenkins \
    -d -m 700m --cpus=0.7 -p 8080:8080 -p 50000:50000 \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v $(which docker):/usr/bin/docker \
    -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts

reduce gradle memory using on jenkins pipeline:
Manage Jenkins=>Configure System=>Global properties=>Environment Variables add:
   name:GRADLE_OPTS
   value:-Xmx256m
###########################################################################




