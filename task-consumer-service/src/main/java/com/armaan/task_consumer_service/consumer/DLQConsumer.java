package com.armaan.task_consumer_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DLQConsumer {

    @KafkaListener(topics = "task-created-dlt", groupId = "dlq-group")
    public void consume(String message){
        System.out.println("Received from DLQ: " + message);
    }
}
