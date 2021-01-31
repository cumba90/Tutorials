set KAFKA_HOME=E:\kafka\confluent-6.0.1
%KAFKA_HOME%\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic nse-eod-topic --from-beginning