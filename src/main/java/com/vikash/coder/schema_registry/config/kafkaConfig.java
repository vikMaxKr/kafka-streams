package com.vikash.coder.schema_registry.config;

import com.vikash.coder.schema_registry.dto.Employee;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class kafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @Value("${spring.kafka.schema-registry-url}")
    private String schemaRegistryUrl;


    @Value("${kafka.consumer.group-id:avro-group}")
    private String consumerGroupId;

//    @Value("${kafka.sasl.jaas-config}")
//    private String jaasConfig;
//
//
//    @Value("${kafka.sasl.mechanism}")
//    private String saslMechanism;
//
//
//    @Value("${kafka.sasl.security-protocol}")
//    private String securityProtocol;


// -------------------- Producer configs & beans --------------------


   // @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        // Setting schema auto-registration to false since we already registered the schema manually following best practice
        //props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, false);


        props.put("basic.auth.credentials.source", "USER_INFO");
        props.put("basic.auth.user.info", "F6EUNWPZVXZDZYHW:cfltPCvxTCJL1N8rpyHwYo/egzM73H71QZo9o5xuKIU3hcq2NJqd+DqkWcuPNx2w");

        // SASL configuration
        props.put("security.protocol", "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username='RTZ2EQDGTN73EJGI' password='cfltO9757PnG3uXggXbQgY5By30QBqV2O5csbaHp3oKN1MRBsh9SOi/c9u1UMBOA';");


        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        return props;
    }


 //   @Bean
    public ProducerFactory<String, Employee> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }


//    @Bean
    public KafkaTemplate<String, Employee> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


// -------------------- Consumer configs & beans --------------------


 //   @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
// Important: return specific records (true) so deserializer returns generated Avro class (e.g. com.example.avro.User)
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
// Optional consumer behavior
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }


//    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

}
