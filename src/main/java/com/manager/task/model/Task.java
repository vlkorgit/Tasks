package com.manager.task.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Task {
	int id;
	@NotBlank
	@NotNull
	@NotEmpty
	String name;
	String description;
	boolean status;

	public Task() {
	}

	public Task(int id, String name, String description, boolean status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
	}

}

