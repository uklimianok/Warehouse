package com.warehouse.demo.util.test.kafka;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class KafkaProducerRunner implements CommandLineRunner {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override 
    public void run(String... args) throws Exception {
        kafkaTemplate.send("test-topic", LocalDateTime.now() + " Kafka is running ...");
    }
}
