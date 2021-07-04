Rem %KAFKA_HOME%\bin\windows\kafka-topics.bat --describe  --zookeeper localhost:2181 --topic invoice
Rem %KAFKA_HOME%\bin\windows\kafka-topics.bat --describe --topic hello-producer-topic-1 --bootstrap-server localhost:9092
%KAFKA_HOME%\bin\windows\kafka-topics.bat --describe --topic hello-producer-topic-2 --bootstrap-server localhost:9092
