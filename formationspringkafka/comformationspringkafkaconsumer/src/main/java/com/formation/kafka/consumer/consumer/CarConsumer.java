package com.formation.kafka.consumer.consumer;

import com.formation.kafka.consumer.service.CarService;
import model.CarMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

public class CarConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CarConsumer.class);

	private CarService carService;

	public CarConsumer(CarService service) {
		this.carService = service;
	}

	@KafkaListener(topics = "${kafka.topic.car}", group = "${kafka.topic.default-group-id}", containerFactory = "carListenerContainerFactory")
	public void consumeMessage(CarMessage model, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		try {
			logger.info("Received message from {} with model {}", topic, model.toString());
			carService.saveCar(model);
			//do some stuff in here
		} catch(Exception ex) {
			logger.error("Error ", ex);
		}
	}
}
