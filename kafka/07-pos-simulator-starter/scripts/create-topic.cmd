set KAFKA_HOME=C:\kafka\confluent-6.0.1
%KAFKA_HOME%\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 3 --partitions 3 --topic pos --config min.insync.replicas=2