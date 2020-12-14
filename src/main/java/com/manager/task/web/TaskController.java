package com.manager.task.web;

import com.manager.task.data.ITaskRepo;
import com.manager.task.queue.JobQueue;
import com.manager.task.model.Task;
import com.manager.task.queue.TokenAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	@Autowired
	ITaskRepo taskRepo;
	@Autowired
	JobQueue jobQueue;

	@GetMapping
	public ResponseEntity<Object> getTasks(@RequestParam(required = false) Boolean status,
	                                       @RequestParam(required = false) String filter) {
		Collection<Task> tasks;
		try {
			if (filter == null) {
				if (status == null)
					tasks = taskRepo.getTasks();
				else
					tasks = taskRepo.getTasksFilteredByStatus(status);
			} else {
				if (status == null)
					tasks = taskRepo.getTasksFilteredByName(filter);
				else
					tasks = taskRepo.getTasksFilteredByStatusAndName(status, filter);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<String> getTask(@PathVariable int id) {
		Task task;
		try {
			task = taskRepo.getTaskById(id);
		} catch (Exception ex) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Object> addTasks(@Valid Task task, Errors errors) {
		if (errors.hasErrors()) return new ResponseEntity<>("Validation error", HttpStatus.BAD_REQUEST);
		int token = jobQueue.addJob(() -> {
				//Thread.sleep(10000);//work simulation
				taskRepo.addTask(task);
				return 0;
		});
		return new ResponseEntity<>(new TokenAnswer(token), HttpStatus.ACCEPTED);
	}

	@PostMapping("/update")
	public ResponseEntity<Object> postEditTask(@Valid Task task, Errors errors) {
		if (errors.hasErrors()) return new ResponseEntity<>("Validation error", HttpStatus.BAD_REQUEST);
		int token = jobQueue.addJob(() -> {
			//Thread.sleep(10000);//work simulation
			taskRepo.updateTask(task);
			return 0;
		});
		return new ResponseEntity<>(new TokenAnswer(token), HttpStatus.ACCEPTED);
	}

	//redundant
	@GetMapping("/{id}/updateStatus")
	public ResponseEntity<Object> updateTaskStatus(@PathVariable int id, @RequestParam(required =
			true) boolean status) {
		int token = jobQueue.addJob(() -> {
			taskRepo.updateTaskStatus(id, status);
			return 0;
		});
		return new ResponseEntity<>(new TokenAnswer(token), HttpStatus.ACCEPTED);
	}
}
