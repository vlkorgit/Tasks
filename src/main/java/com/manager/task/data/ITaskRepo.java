package com.manager.task.data;

import com.manager.task.model.Task;

import java.util.Collection;

public interface ITaskRepo {
	Collection<Task> getTasks();
	Collection<Task> getTasksFilteredByStatus(boolean status);
	Collection<Task> getTasksFilteredByName(String name);
	Collection<Task> getTasksFilteredByStatusAndName(boolean status, String name);
	void updateTask(Task task);
	void updateTaskStatus(int taskId, boolean status);
	Task getTaskById(int id);
	void addTask(Task task);
}
