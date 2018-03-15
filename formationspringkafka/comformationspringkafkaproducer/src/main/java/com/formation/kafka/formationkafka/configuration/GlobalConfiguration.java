package com.formation.kafka.formationkafka.configuration;

import com.formation.kafka.formationkafka.mapper.CarDtoToCarMessageMapper;
import com.formation.kafka.formationkafka.producer.Sender;
import com.formation.kafka.formationkafka.service.CarService;
import com.formation.kafka.formationkafka.service.impl.CarServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mohammed_eznati on 27/11/2017.
 */
@Configuration
public class GlobalConfiguration {

	@Bean
	public CarService carService(Sender sender, CarDtoToCarMessageMapper mapper) {
		return new CarServiceImpl(sender, mapper);
	}

	@Bean
	public CarDtoToCarMessageMapper carDtoToCarMessageMapper(){
		return new CarDtoToCarMessageMapper();
	}

}
