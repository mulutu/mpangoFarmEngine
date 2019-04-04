package com.mpangoEngine.core.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mpangoEngine.core.util.JsonDateDeserializer;
import com.mpangoEngine.core.util.JsonDateSerializer;

@Component
public class Task implements Serializable {
	private int taskId;
	private int projectId;
	private String taskName;
	private String description;
	private Date taskDate;
	private int priority;
	private boolean active;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getTaskDate() {
		return taskDate;
	}
	@JsonDeserialize(using = JsonDateDeserializer.class, as = Date.class)
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}	
}