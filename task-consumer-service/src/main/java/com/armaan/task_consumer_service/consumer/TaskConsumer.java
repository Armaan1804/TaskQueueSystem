package com.armaan.task_consumer_service.consumer;

import com.armaan.task_consumer_service.event.TaskCreatedEvent;
import com.armaan.task_consumer_service.service.TaskProcessingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import com.armaan.task_consumer_service.service.TaskProcessingService;
import com.armaan.task_consumer_service.service.DLQProducer;
@Service
public class TaskConsumer {
    private final TaskProcessingService taskProcessingService;
    private final ObjectMapper objectMapper;
    private final DLQProducer dlqProducer;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    public TaskConsumer(
            ObjectMapper objectMapper,
            TaskProcessingService taskProcessingService,
            DLQProducer dlqProducer) {

        this.objectMapper = objectMapper;
        this.taskProcessingService = taskProcessingService;
        this.dlqProducer = dlqProducer;
    }

    @KafkaListener(topics = "task-created", groupId = "task-group")
    public void consume(String message) {

        try {

            TaskCreatedEvent event =
                    objectMapper.readValue(message, TaskCreatedEvent.class);

            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {

                try {

                    taskProcessingService.process(event);

                    System.out.println("Task processed successfully!");
                    break;

                } catch (Exception e) {

                    System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                    if (attempt == 3) {

                        System.out.println("All retry attempts failed.");

                        dlqProducer.sendToDLQ(message);
                    }

                    try {
                        Thread.sleep(RETRY_DELAY_MS); // Wait 1 second before retrying
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Invalid message received: " + e.getMessage());
        }
    }
}