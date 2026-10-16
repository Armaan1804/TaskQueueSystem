package com.armaan.task_consumer_service.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreatedEvent {

    private Long taskId;
    private String title;
    private String description;

}
