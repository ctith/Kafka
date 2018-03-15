package com.formation.kafka.formationkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class FormationkafkaApplication extends SpringBootServletInitializer {

/*
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FormationkafkaApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(FormationkafkaApplication.class, args);
	}

	//*/

	private final static Logger logger = LoggerFactory.getLogger(FormationkafkaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FormationkafkaApplication.class, args);
		logger.info("###########################################");
		logger.info("#  PRODUCER Server started");
		logger.info("###########################################");
	}

}
