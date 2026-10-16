package com.arman.taskproducer.service;


import com.arman.taskproducer.dto.CreateTaskRequest;
import com.arman.taskproducer.entity.OutboxEvent;
import com.arman.taskproducer.entity.Task;
import com.arman.taskproducer.enums.OutboxStatus;
import com.arman.taskproducer.enums.TaskStatus;
import com.arman.taskproducer.event.TaskCreatedEvent;
import com.arman.taskproducer.exception.TaskNotFoundException;
import com.arman.taskproducer.repository.OutboxEventRepository;
import com.arman.taskproducer.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;



@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public TaskService(TaskRepository taskRepository, OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper){
        this.taskRepository = taskRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Task createTask(CreateTaskRequest request) throws JacksonException {
        Task newTask = new Task();
        newTask.setTitle(request.getTitle());
        newTask.setDescription(request.getDescription());
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setStatus(TaskStatus.CREATED);
        newTask.setPriority(request.getPriority());
        newTask.setUpdatedAt(LocalDateTime.now());

        Task savedTask =  taskRepository.save(newTask);
        TaskCreatedEvent event = TaskCreatedEvent.builder()
                .taskId(savedTask.getId())
                .title(savedTask.getTitle())
                .priority(savedTask.getPriority())
                .build();

        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .eventType("TASK_CREATED")
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        outboxEventRepository.save(outboxEvent);

        return savedTask;

    }
    public Task getTaskById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));


    }
}
