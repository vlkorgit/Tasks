package com.manager.task.data;

import com.manager.task.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class TasksRepo implements ITaskRepo {

	JdbcTemplate jdbc;

	@Autowired
	public TasksRepo(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Collection<Task> getTasks() {
		return jdbc.query("select * from tasks",
				this::taskMapper);
	}
	@Override
	public Collection<Task> getTasksFilteredByStatusAndName(boolean status, String nameFilter) {
		return jdbc.query(
				"select * from tasks where status =" + status + " and name like '" + nameFilter + "'", this::taskMapper);
	}

	@Override
	public void updateTask(Task task) {
		jdbc.update("update tasks set name=?,description=?,status=? where id=?", task.getName(),task.getDescription(),
				task.isStatus(),task.getId());
	}

	@Override
	public void updateTaskStatus(int taskId, boolean status) {
		jdbc.update("update tasks set status=? where id = ?",status,taskId);
	}

	@Override
	public Task getTaskById(int id) {
		return jdbc.queryForObject("select * from tasks where tasks.id=?",
				this::taskMapper, id);

	}

	@Override
	public void addTask(Task task) {
		jdbc.update("insert into tasks (name,description,status) values (?,?,?)", task.getName(),
				task.getDescription(),
				task.isStatus());
		//throw new RuntimeException();//db exception simulation
	}

	@Override
	public Collection<Task> getTasksFilteredByStatus(boolean status) {
		return jdbc.query("select * from tasks where status=?", this::taskMapper, status);
	}

	@Override
	public Collection<Task> getTasksFilteredByName(String name) {
		return jdbc.query("select * from tasks where name like '" + name + "'", this::taskMapper);
	}

	private Task taskMapper(ResultSet rs, int rowNum) throws SQLException {
		return new Task(
				rs.getInt("id"),
				rs.getString("name"),
				rs.getString("description"),
				rs.getBoolean("status"));
	}

}
