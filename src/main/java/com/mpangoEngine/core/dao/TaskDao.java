package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Task;

@Transactional
@Repository
public interface TaskDao {
	
	Task getTaskById(int taskId);
	
	List<Task> getAllTasks();
	
	boolean existsById(int taskId);
	
	int saveTask(Task task);
	
	int updateTask(Task task);
	
	List<Task> getTasksForProject(int projectId);
}
