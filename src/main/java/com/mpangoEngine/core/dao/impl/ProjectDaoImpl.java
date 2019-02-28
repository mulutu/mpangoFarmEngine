package com.mpangoEngine.core.dao.impl;

import java.math.BigDecimal;
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

import com.mpangoEngine.core.dao.ProjectDao;
import com.mpangoEngine.core.model.Project;

@Component
@Transactional
public class ProjectDaoImpl extends JdbcDaoSupport implements ProjectDao {

	public static final Logger logger = LoggerFactory.getLogger(ProjectDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired 
	public ProjectDaoImpl(DataSource dataSource) {
	    super();
	    setDataSource(dataSource);
	}

	@Override
	public List<Map<String, Object>> findProjectDetails(int projid) {
		String query = "SELECT p.id, p.date_created, p.description as 'project_desc', p.project_name, p.user_id, p.farm_id, p.actual_output, p.expected_output, u.unit_description,  "
				+ "f.farm_name, f.description "
				+ "FROM project p, farm f , unit_of_output u  " 
				+ "WHERE f.id=p.farm_id  AND p.unit_id = u.id AND  p.id = " + projid;
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		return rows;
	}

	@Override
	public Project findProjectById(int id) {
		String query = "SELECT * FROM project WHERE id = ?";
		Project project = (Project) jdbcTemplate.queryForObject(query, new Object[] { id }, new BeanPropertyRowMapper(Project.class));
		return project;
	}

	@Override
	public List<Project> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public boolean existsById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int save(Project project) {
		String sql = "INSERT INTO project "
				+ "(`id`, `date_created`, `description`, `project_name`, `user_id`, `farm_id`, `actual_output`, `expected_output`, `unit_id`) "
				+ "VALUES (?, ?, ?, ?, ?, ? ,? , ?, ?)";
		
		
		logger.debug("ProjectDaoImpl->save() >>> getId {} ", project.getId());
		logger.debug("ProjectDaoImpl->save() >>> getDateCreated {} ", project.getDateCreated());
		logger.debug("ProjectDaoImpl->save() >>> getDescription {} ", project.getDescription());
		logger.debug("ProjectDaoImpl->save() >>> getProjectName {} ", project.getProjectName());
		logger.debug("ProjectDaoImpl->save() >>> getUserId {} ", project.getUserId());
		logger.debug("ProjectDaoImpl->save() >>> getFarmId {} ", project.getFarmId());
		logger.debug("ProjectDaoImpl->save() >>> getActualOutput {} ", project.getActualOutput());
		logger.debug("ProjectDaoImpl->save() >>> getExpectedOutput {} ", project.getExpectedOutput());
		logger.debug("ProjectDaoImpl->save() >>> getUnitId {} ", project.getUnitId() );
		
		Object[] params = { project.getId(), new Date(), project.getDescription(), project.getProjectName(),
				project.getUserId(), project.getFarmId(), project.getActualOutput(), project.getExpectedOutput(), 1 };
		int[] types = {Types.INTEGER, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };

		return getJdbcTemplate().update( sql, params, types );	
	}

	@Override
	public List<Project> findAllProjectsByUser(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
