package com.arman.taskproducer.repository;

import com.arman.taskproducer.entity.OutboxEvent;
import com.arman.taskproducer.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent,Long> {

    List<OutboxEvent> findByStatus(OutboxStatus outboxStatus);
}
