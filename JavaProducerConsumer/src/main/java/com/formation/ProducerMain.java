package com.formation;

/**
 * Created by mohammed_eznati on 09/03/2018.
 */

import java.util.Properties;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerMain {

    private final static String TOPIC = "test";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092";

    public static void main(String[] args) throws Exception {
        runProducerAsync(10);
    }

    private static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new KafkaProducer<Long, String>(props);
    }

    static void runProducer(final int sendMessageCount) throws Exception {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();

        try {
            for (long index = time; index < time + sendMessageCount; index++) {
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(TOPIC, index,
                                "Hello Mom " + index);

                RecordMetadata metadata = producer.send(record).get();

                long elapsedTime = System.currentTimeMillis() - time;
                System.out.println("#########################################");
                System.out.printf("sent record(key=%s value=%s) " +
                                "meta(partition=%d, offset=%d) time=%d\n",
                        record.key(), record.value(), metadata.partition(),
                        metadata.offset(), elapsedTime);
                Thread.sleep(5000);
            }
        } finally {
            producer.flush();
            producer.close();
        }
    }

    static void runProducerAsync(final int sendMessageCount) throws Exception {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();

        try {
            for (long index = time; index < time + sendMessageCount; index++) {
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(TOPIC, index,
                                "Hello Mom " + index);

                producer.send(record, (recordMetadata, exception) ->{
                    long elapsedTime = System.currentTimeMillis() - time;
                    if (recordMetadata != null){
                        System.out.println("#################### ASYNC #####################");
                        System.out.printf("sent record(key=%s value=%s) " +
                                        "meta(partition=%d, offset=%d) time=%d\n",
                                record.key(), record.value(), recordMetadata.partition(),
                                recordMetadata.offset(), elapsedTime);
                    }else{
                        exception.printStackTrace();
                    }
                } );

                Thread.sleep(5000);

            }
        } finally {
            producer.flush();
            producer.close();
        }
    }
}