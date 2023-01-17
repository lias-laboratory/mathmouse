apt-get update -y
apt-get install openjdk-8-jdk -y 
# install maven
apt-get install -y maven
# adding webupd8team/java repository for further installing of java 8
add-apt-repository ppa:webupd8team/java -y
apt-get update
# install docker
apt-get install apt-transport-https ca-certificates curl software-properties-common -y
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
apt-key fingerprint 0EBFCD88
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
apt-get update -y
apt-get install docker-ce -y
# install docker compose
sudo -i
curl -L https://github.com/docker/compose/releases/download/1.5.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
exit
