package com.warehouse.demo.util.test.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component 
public class KafkaConsumerRunner {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerRunner.class);

    @KafkaListener(groupId = "1", topics = "test-topic")
    public void listen(String in) {
        logger.info(in);
    }
}
