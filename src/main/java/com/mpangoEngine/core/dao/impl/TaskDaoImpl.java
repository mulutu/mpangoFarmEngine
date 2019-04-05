package com.mpangoEngine.core.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.TaskDao;
import com.mpangoEngine.core.model.Project;
import com.mpangoEngine.core.model.Task;

@Component
@Transactional
public class TaskDaoImpl extends JdbcDaoSupport implements TaskDao {

	public static final Logger logger = LoggerFactory.getLogger(ProjectDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public TaskDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Override
	public Task getTaskById(int taskId) {
		String query = "SELECT * FROM tasks WHERE id = ?";
		Task task = (Task) jdbcTemplate.queryForObject(query, new Object[] { taskId },
				new BeanPropertyRowMapper(Task.class));
		return task;
	}

	@Override
	public List<Task> getAllTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(int taskId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int saveTask(Task task) {
		String Query = "INSERT INTO tasks "
				+ "(`id`, `project_id`, `task_name`, `task_date`, `description`, `priority`, `active`) "
				+ "VALUES (?, ?, ?, ?, ?, ? ,?)";		
		
		logger.debug("TaskDaoImpl->saveTask() >>> task {} ", task);
		
		Object[] params = { task.getTaskId(), task.getProjectId(), task.getTaskName(), task.getTaskDate(), task.getDescription(), task.getPriority(), task.isActive() };
		int[] types = {     Types.INTEGER,     Types.INTEGER,      Types.VARCHAR,      Types.DATE, Types.VARCHAR,         Types.INTEGER,      Types.BOOLEAN };

		return getJdbcTemplate().update( Query, params, types );	
	}

	@Override
	public int updateTask(Task task) {
		logger.debug("TaskDaoImpl->updateTask() >>> task {} ", task);
		
		String Query = " UPDATE tasks "
				+ " SET project_id=?, task_name=?, task_date=?, description=?, priority=?, active=? "
				+ " WHERE id=?";		
		
		Object[] params = { task.getProjectId(), task.getTaskName(), task.getTaskDate(), task.getDescription(), task.getPriority(), task.isActive(), task.getTaskId() };
		int[] types = {     Types.INTEGER,       Types.VARCHAR,      Types.DATE,         Types.VARCHAR,         Types.INTEGER,      Types.BOOLEAN,   Types.INTEGER    };

		return getJdbcTemplate().update( Query, params, types );
	}
	
	@Override
	public int deleteTask(int taskId) {
		logger.debug("TaskDaoImpl->deleteTask() >>> taskId {} ", taskId);
		
		String Query = " DELETE FROM tasks WHERE id=?";		
		
		Object[] params = { taskId };
		int[] types = { Types.INTEGER };

		return getJdbcTemplate().update( Query, params, types );
	}

	@Override
	public List<Task> getTasksForProject(int projectId) {
		
		String Query = "SELECT  t.id as task_id, t.project_id, t.task_name, t.task_date, t.description, t.priority, t.active "
				+ "FROM tasks t " + "WHERE t.project_id = " + projectId;
		
		logger.debug("TaskDaoImpl->getTasksForProject() >>> Query {} ", Query);
		
		List<Task> tasks = new ArrayList<Task>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {
			
			Task task = new Task();
			
			task.setTaskId((int) row.get("task_id"));
			task.setProjectId((int) row.get("project_id"));
			task.setTaskName((String) row.get("task_name"));
			task.setTaskDate((Date) row.get("task_date"));
			task.setDescription((String) row.get("description"));			
			task.setPriority((int) row.get("priority"));
			task.setActive((boolean) row.get("active"));

			tasks.add(task);			
		}
		return tasks;
	}
}
