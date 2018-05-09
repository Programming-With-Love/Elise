#! /bin/zsh

if [ $UID -ne 0 ]; then
  echo "Superuser privileges are required to run this script."
  echo "e.g. \"sudo $0\""
  exit 1
fi

zooHome="/opt/zookeeper-3.4.10"
kafkaHome="/opt/kafka_2.12-1.1.0"

source /etc/profile.d/java.sh

echo "starting zookeeper..."
sudo $zooHome/bin/zkServer.sh start $zooHome/conf/zoo.cfg

echo "sleeping 1s"
sleep 1

echo "starting kafka..."
$kafkaHome/bin/kafka-server-start.sh $kafkaHome/config/server.properties
