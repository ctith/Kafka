package com.formation.kafka.formationkafka.producer;

import model.CarMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    private KafkaTemplate<String, CarMessage> kafkaTemplate;

    private String topic;

    public Sender(String topic, KafkaTemplate<String, CarMessage> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void send(CarMessage payload) {
        LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);
        kafkaTemplate.send(this.topic, payload);
    }
}
