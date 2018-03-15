package com.formation.kafka.consumer.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
public class TopicProperties {

	private String car;
	private String defaultGroupId;

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getDefaultGroupId() {
		return defaultGroupId;
	}

	public void setDefaultGroupId(String defaultGroupId) {
		this.defaultGroupId = defaultGroupId;
	}
}
