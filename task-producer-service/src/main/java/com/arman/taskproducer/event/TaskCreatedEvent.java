package com.arman.taskproducer.event;

import com.arman.taskproducer.enums.TaskPriority;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreatedEvent {

    private Long taskId;
    private String title;
    private TaskPriority priority;
}
