package com.formation;

/**
 * Created by mohammed_eznati on 09/03/2018.
 */

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerBasic {

    private final static String TOPIC = "test";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092";


    static void runConsumer() throws InterruptedException {
        final Consumer<Long, String> consumer = createConsumer();
        final int giveUp = 100;   int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords =
                    consumer.poll(1000);
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            consumerRecords.forEach(record -> {
                System.out.println("----------------------------------");
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
                System.out.println("----------------------------------");
            });
            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE");
    }
    public static void main(String[] args) throws Exception {
        //runConsumer();
        consumer_with_partition();
    }

    private static Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());


        // Create the consumer using props.
        final Consumer<Long, String> consumer =
                new KafkaConsumer<>(props);

        // Subscribe to the topic.
        //consumer.subscribe(Collections.singletonList(TOPIC));
        //return consumer;

        consumer.subscribe(Collections.singletonList(TOPIC), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                collection.forEach(topicPartition -> {
                    System.out.println("PARTITION REVOKED => " + topicPartition.partition());
                });
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                collection.forEach(topicPartition -> {
                    System.out.println("PARTITION ASSIGNED => " + topicPartition.partition());
                });
            }
        });

        return consumer;

    }

    private static void consumer_with_partition(){
        final Consumer<Long, String> consumer = createConsumer();
        while (true){

            ConsumerRecords<Long, String> records = consumer.poll(Long.MAX_VALUE);
            for (TopicPartition partition : records.partitions()) {
                System.out.println("===============> PARTITION "+ partition.partition());
                List<ConsumerRecord<Long, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<Long, String> record : partitionRecords) {
                    System.out.println("-----------------------------");
                    System.out.println(record.offset() + ": " + record.value());
                }
                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
            }
        }

    }
}
