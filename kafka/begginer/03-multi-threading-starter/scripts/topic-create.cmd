set KAFKA_HOME=E:\kafka\confluent-6.0.1
%KAFKA_HOME%\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --topic nse-eod-topic --partitions 5 --replication-factor 3