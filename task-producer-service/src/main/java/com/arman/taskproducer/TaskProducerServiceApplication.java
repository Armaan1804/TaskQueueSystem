package com.arman.taskproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TaskProducerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskProducerServiceApplication.class, args);
	}

}
