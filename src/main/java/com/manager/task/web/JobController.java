package com.manager.task.web;

import com.manager.task.queue.JobQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {
	@Autowired
	JobQueue jobsQueue;
	@GetMapping("/{token}")
	public ResponseEntity<Object> getOperationStatus(@PathVariable int token) {
		Object result = jobsQueue.getJobStatus(token);
		if (result ==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<Object> getJobs(){
		return new ResponseEntity<>(jobsQueue,HttpStatus.OK);
	}

}
