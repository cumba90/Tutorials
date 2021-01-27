set KAFKA_HOME=C:\kafka\confluent-6.0.1
%KAFKA_HOME%\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --topic hello-producer-topic --partitions 5 --replication-factor 3