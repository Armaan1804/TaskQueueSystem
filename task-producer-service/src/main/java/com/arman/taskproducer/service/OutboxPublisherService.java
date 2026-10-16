package com.arman.taskproducer.service;

import com.arman.taskproducer.entity.OutboxEvent;
import com.arman.taskproducer.enums.OutboxStatus;
import com.arman.taskproducer.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OutboxPublisherService {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisherService(
            OutboxEventRepository outboxEventRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {

        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {

        List<OutboxEvent> pendingEvents =
                outboxEventRepository.findByStatus(OutboxStatus.PENDING);

        for (OutboxEvent event : pendingEvents) {

            try {

                CompletableFuture<SendResult<String, String>> future =
                        kafkaTemplate.send("task-created", event.getPayload());

                future.whenComplete((result, exception) -> {

                    if (exception == null) {
                        event.setStatus(OutboxStatus.PUBLISHED);
                        event.setPublishedAt(LocalDateTime.now());
                        outboxEventRepository.save(event);
                    } else {
                        event.setStatus(OutboxStatus.FAILED);
                        outboxEventRepository.save(event);
                    }

                });

            } catch (Exception e) {

                event.setStatus(OutboxStatus.FAILED);
                outboxEventRepository.save(event);

            }
        }
    }
}