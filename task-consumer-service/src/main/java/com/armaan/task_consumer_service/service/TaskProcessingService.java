package com.armaan.task_consumer_service.service;

import com.armaan.task_consumer_service.event.TaskCreatedEvent;
import org.springframework.stereotype.Service;
import com.armaan.task_consumer_service.service.TaskProcessingService;
@Service
public class TaskProcessingService {

    public void process(TaskCreatedEvent event) {

        System.out.println("Processing Task...");

        throw new RuntimeException("Database is down!");

    }
}
