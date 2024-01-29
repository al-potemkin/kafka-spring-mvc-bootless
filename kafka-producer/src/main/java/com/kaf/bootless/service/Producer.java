package com.kaf.bootless.service;

import com.kaf.bootless.config.AvroRecordSerializer;
import com.kaf.bootless.config.Const;
import com.kaf.bootless.repositroy.SchemaRepository;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class Producer {
    public void sendMessage(String msg) {
        KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(settingProperties());
        ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(settingProperties()
                .getProperty(Const.TOPIC_PROP_CONF), avro(msg));
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
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Const.KAFKA_URL);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroRecordSerializer.class);

//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
//        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(Const.TOPIC_PROP_CONF, Const.TOPIC_NAME);

        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(Const.SCHEMA_PROP_CONF, SchemaRepository.instance().getSchemaObject());

        return props;
    }

    private GenericRecord avro(String msg) {
//        String userSchema = "{\"type\":\"record\"," +
//                "\"name\":\"myrecord\"," +
//                "\"fields\":[{\"name\":\"msg\",\"type\":\"string\"}]}";
//        Schema.Parser parser = new Schema.Parser();
//        Schema schema = parser.parse(userSchema);
//        GenericRecord avroRecord = new GenericData.Record(schema);
        GenericRecord avroRecord = new GenericData.Record(SchemaRepository.instance().getSchemaObject());
        avroRecord.put("msg", msg);

        System.out.println("###----Record: " + avroRecord);
        return avroRecord;
    }
}
