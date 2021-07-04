package com.techleads.app.producer;

public interface AppConfig {
    String applicationId = "HelloProducer";
    String bootstrapServers = "localhost:9092,localhost:9093";
    String topicName1 = "hello-producer-topic-1";
    String topicName2 = "hello-producer-topic-2";
    int numEvents = 2;
    String transactionId = "Hello-Producer-Tx";

}
