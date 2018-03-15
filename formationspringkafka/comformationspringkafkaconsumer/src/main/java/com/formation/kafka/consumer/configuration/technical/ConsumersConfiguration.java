package com.formation.kafka.consumer.configuration.technical;

import com.formation.kafka.consumer.consumer.CarConsumer;
import com.formation.kafka.consumer.repository.CarRepository;
import com.formation.kafka.consumer.service.CarService;
import com.formation.kafka.consumer.service.impl.CarServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class ConsumersConfiguration {


	@Bean
	public CarConsumer carConsumer(CarService service) {
		return new CarConsumer(service);
	}

	@Bean
	public CarService carService(CarRepository repository){
		return new CarServiceImpl(repository);
	}
}
