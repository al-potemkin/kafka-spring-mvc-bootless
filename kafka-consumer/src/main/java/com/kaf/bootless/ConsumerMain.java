package com.kaf.bootless;

import com.kaf.bootless.config.Const;
import com.kaf.bootless.repository.SchemaRepository;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class ConsumerMain {
    public static void main(String[] args) {
        System.out.println("--- Kafka Consumer ---");

        // create consumer configs
        Properties properties = consumerConfig();

        // create consumer
        final KafkaConsumer<String, User> consumer = new KafkaConsumer<>(properties);

        // get a reference to the current thread
        final Thread mainThread = Thread.currentThread();

        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            // subscribe consumer to our topic(s)
            consumer.subscribe(Collections.singletonList(Const.TOPIC_NAME));
            System.out.println(String.format("Subscribed on %s topic", Const.TOPIC_NAME));

            // poll for new data
            while (true) {
                ConsumerRecords<String, User> records = consumer.poll(100);
                for (ConsumerRecord<String, User> record : records) {
                    System.out.println("Key: " + record.key() + ", Value: " + record.value());
                    System.out.println("Partition: " + record.partition() + ", Offset:" + record.offset());
                }
            }

        } catch (WakeupException e) {
            System.out.println("Wake up exception!");
            // ignore this as this is an expected exception when closing a consumer
        } catch (Exception e) {
            System.out.println("Unexpected exception" + e);
        } finally {
            consumer.close(); // this will also commit the offsets if need be.
            System.out.println("The consumer is now gracefully closed.");
        }

    }

    private static Properties consumerConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Const.KAFKA_URL);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroRecordDeserializer.class);

        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, Const.SCHEMA_REGISTRY_URL);

        properties.put(Const.SCHEMA_PROP_CONF, SchemaRepository.instance().getSchemaObject());
        properties.put(Const.TOPIC_PROP_CONF, Const.TOPIC_NAME);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, Const.GROUP_NAME);

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");

//        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }
}