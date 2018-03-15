package com.formation;

/**
 * Created by mohammed_eznati on 09/03/2018.
 */

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;

public class KafkaConsumerMainJson {

	private final static String TOPIC = "test";
	private final static String BOOTSTRAP_SERVERS =
			"localhost:9092";


	static void runConsumer() throws InterruptedException {
		final Consumer<Long, JsonNode> consumer = createConsumer();
		final int giveUp = 100;   int noRecordsCount = 0;
		while (true) {
			final ConsumerRecords<Long, JsonNode> consumerRecords =
					consumer.poll(1000);
			if (consumerRecords.count()==0) {
				noRecordsCount++;
				if (noRecordsCount > giveUp) break;
				else continue;
			}
			consumerRecords.forEach(record -> {
				System.out.println("#######################################");
				System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
						record.key(), record.value(),
						record.partition(), record.offset());
			});
			consumer.commitAsync();
		}
		consumer.close();
		System.out.println("DONE");
	}
	public static void main(String[] args) throws Exception {
		runConsumer();
	}

	private static Consumer<Long, JsonNode> createConsumer() {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,
				"KafkaExampleConsumer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				JsonDeserializer.class.getName());
		// Create the consumer using props.
		final Consumer<Long, JsonNode> consumer =
				new KafkaConsumer<>(props);
		// Subscribe to the topic.
		consumer.subscribe(Collections.singletonList(TOPIC));
		return consumer;
	}
}