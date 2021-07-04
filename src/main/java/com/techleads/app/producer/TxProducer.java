package com.techleads.app.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TxProducer {
    private static final Logger logger = LoggerFactory.getLogger(TxProducer.class);

    public static void main(String[] args) {
        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfig.applicationId);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, AppConfig.transactionId);


        KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);
        producer.initTransactions();
        logger.info("Start sending messages... starting first transaction");
        producer.beginTransaction();
        try {
            for (int i = 1; i <= AppConfig.numEvents; i++) {
                producer.send(new ProducerRecord<>(AppConfig.topicName1, i, "Simple message-T1 : " + i));
                producer.send(new ProducerRecord<>(AppConfig.topicName2, i, "Simple message-T1 : " + i));
            }
            logger.info("Committing First Transaction.");
            producer.commitTransaction();

        } catch (Exception e) {
            logger.error("Aborting first Transaction");
            producer.abortTransaction();
            producer.close();
            throw new RuntimeException(e);
        }

        logger.info("Start sending messages... starting 2nd transaction");
        producer.beginTransaction();
        try {
            for (int i = 1; i <= AppConfig.numEvents; i++) {
                producer.send(new ProducerRecord<>(AppConfig.topicName1, i, "Simple message-T2 : " + i));
                producer.send(new ProducerRecord<>(AppConfig.topicName2, i, "Simple message-T2 : " + i));
            }

            logger.info("Aborting Second Transaction.");
            producer.abortTransaction();


        } catch (Exception e) {
            logger.error("Aborting SECOND Transaction");
            producer.abortTransaction();
            producer.close();
            throw new RuntimeException(e);
        }

        logger.info("Finished - Closing Kafka Producer.");
        producer.close();
    }
}
