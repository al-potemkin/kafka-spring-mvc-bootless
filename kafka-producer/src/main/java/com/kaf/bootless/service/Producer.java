package com.kaf.bootless.service;

import com.kaf.bootless.Model;
import com.kaf.bootless.config.Const;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class Producer {
    public void sendMessage(String message) {
        KafkaProducer<String, Model> producer = new KafkaProducer<>(settingProperties());

        Model object = Model.newBuilder()
                .setMessage(message)
                .build();

        String topicName = settingProperties().getProperty(Const.TOPIC_PROP_CONF);
        ProducerRecord<String, Model> record = new ProducerRecord<>(topicName, object);
        try {
            producer.send(record);
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e);
        } finally {
            producer.flush();
            producer.close();
        }

    }

    private Properties settingProperties() {
        Properties props = new Properties();
        props.put(Const.TOPIC_PROP_CONF, Const.TOPIC_NAME);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Const.KAFKA_URL);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, Const.SCHEMA_REGISTRY_URL);
        props.put(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, true);
        return props;
    }
}
