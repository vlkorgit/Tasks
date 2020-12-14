package com.manager.task;

import com.manager.task.queue.JobQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	@Bean
	@Scope("singleton")
	public JobQueue getJobsQueue(){
		return new JobQueue();
	}
}
