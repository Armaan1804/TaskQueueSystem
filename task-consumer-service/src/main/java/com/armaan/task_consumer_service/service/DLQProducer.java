package com.armaan.task_consumer_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DLQProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;

    public void sendToDLQ(String message){
        kafkaTemplate.send("task-created-dlt",message);
        System.out.println("Message sent to DLQ : " + message);


    }
}
