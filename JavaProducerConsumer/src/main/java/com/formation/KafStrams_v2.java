package com.formation;

/**
 * Created by mohammed_eznati on 09/03/2018.
 */

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
		import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.JsonNode;

public class KafStrams_v2 {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

		// load a simple json serializer
//		final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
//		// load a simple json deserializer
//		final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
//		// use the simple json serializer and deserialzer we just made and load a Serde for streaming data
//		final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

		// setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
		// Note: To re-run the demo, you need to use the offset reset tool:
		// https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Application+Reset+Tool
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		StreamsBuilder builder = new StreamsBuilder();

		KStream<String, String> source = builder.stream("topic-in");


//
		// Construct a `KStream` from the input topic "streams-plaintext-input", where message values
		// represent lines of text (for the sake of this example, we ignore whatever may be stored
		// in the message keys).
//		KStream<String, String> textLines = builder.stream("topic-in");
		source.foreach((item, val) -> doSomth(item, val));

//		KTable<String, Long> wordCounts = source
//				// Split each text line, by whitespace, into words.
//				.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
//
//				// Group the text words as message keys
//				.groupBy((key, value) -> value)
//
//				// Count the occurrences of each word (message key).
//				.count();

		// Store the running counts as a changelog stream to the output topic.
//		wordCounts.toStream().to("topic-out");
//		wordCounts.toStream().to("topic-out-2", Produced.with(Serdes.String(), Serdes.Long()));

//		wordCounts.toStream().foreach((item, val )-> System.out.print(item.toString() + "################# "+ val.toString()));

//		final StreamsBuilder builder = new StreamsBuilder();
//
////		 Construct a `KStream` from the input topic "streams-plaintext-input", where message values
//				// represent lines of text (for the sake of this example, we ignore whatever may be stored
//				// in the message keys).
//				KStream<String, String> textLines = builder.stream("topic-in",
//						Consumed.with(stringSerde, stringSerde));
//
//				KTable<String, Long> wordCounts = textLines
//						// Split each text line, by whitespace, into words.
//						.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
//
//						// Group the text words as message keys
//						.groupBy((key, value) -> value)
//
//						// Count the occurrences of each word (message key).
//						.count();
//
//				// Store the running counts as a changelog stream to the output topic.
//				wordCounts.toStream().to("topic-out", Produced.with(Serdes.String(), Serdes.Long()));
//
//
////		builder.stream("topic-in").to("topic-out");

		final Topology topology = builder.build();

		final KafkaStreams streams = new KafkaStreams(builder.build(), props);

		final CountDownLatch latch = new CountDownLatch(1);

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				streams.close();
				latch.countDown();
			}
		});

		try {
			streams.start();
			latch.await();
		} catch (Throwable e) {

			System.exit(1);
		}
		System.exit(0);
	}

	private static void doSomth(String item, String val) {
			System.out.println("#################################");
			System.out.println("ITEM = "+ item);
			System.out.println("VALUE = "+ val);
			System.out.println("#################################");

	}
}