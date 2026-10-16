package com.arman.taskproducer.repository;

import com.arman.taskproducer.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
