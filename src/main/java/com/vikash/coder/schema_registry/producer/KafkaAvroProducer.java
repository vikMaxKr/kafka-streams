package com.vikash.coder.schema_registry.producer;

import com.vikash.coder.schema_registry.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaAvroProducer {

    @Value("${topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, Employee> template;


  //  @Autowired
    public void send(){

        Employee employee1=new Employee();
        employee1.setId("232131");
        employee1.setEmailId("vikash112@gmial.com");
        employee1.setFirstName("vikash");
        employee1.setLastName("kumar");
        employee1.setMiddleName("babua");
        employee1.setLocalAddress("daudpur");

        CompletableFuture<SendResult<String, Employee>> future = template.send(topicName, UUID.randomUUID().toString(),employee1);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + employee1 +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        employee1 + "] due to : " + ex.getMessage());
            }
        });
    }
}
