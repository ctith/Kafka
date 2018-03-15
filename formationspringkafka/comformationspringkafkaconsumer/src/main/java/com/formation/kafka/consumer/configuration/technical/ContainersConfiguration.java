package com.formation.kafka.consumer.configuration.technical;

import com.formation.kafka.consumer.configuration.properties.KafkaProperties;
import com.formation.kafka.consumer.configuration.properties.TopicProperties;
import model.CarMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ContainersConfiguration {


	private final KafkaProperties kafkaProperties;
	private final TopicProperties topicProperties;

	public ContainersConfiguration(KafkaProperties kafkaProperties, TopicProperties topicProperties) {
		this.kafkaProperties = kafkaProperties;
		this.topicProperties = topicProperties;
	}

	/**@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ComutitreModel> comutitreListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ComutitreModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(comutitreConsumerFactory());
		return factory;
	}**/

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, CarMessage> carListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, CarMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(carConsumerFactory());
		return factory;
	}

	/**
	private ConsumerFactory<String, ComutitreModel> comutitreConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost() + ":" + kafkaProperties.getPort());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, topicProperties.getDefaultGroupId());
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(ComutitreModel.class));
	}**/

	private ConsumerFactory<String, CarMessage> carConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost() + ":" + kafkaProperties.getPort());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, topicProperties.getDefaultGroupId());
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(CarMessage.class));
	}

}
