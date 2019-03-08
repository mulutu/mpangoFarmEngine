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
import com.mpangoEngine.core.model.Transaction;

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
		String sql = "INSERT INTO projects "
				+ "(`id`, `date_created`, `description`, `project_name`, `user_id`, `farm_id`, `actual_output`, `expected_output`, `unit_id`) "
				+ "VALUES (?, ?, ?, ?, ?, ? ,? , ?, ?)";		
		
		logger.debug("ProjectDaoImpl->save() >>> project {} ", project);
		
		Object[] params = { project.getId(), new Date(), project.getDescription(), project.getProjectName(),
				project.getUserId(), project.getFarmId(), project.getActualOutput(), project.getExpectedOutput(), 1 };
		int[] types = {Types.INTEGER, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };

		return getJdbcTemplate().update( sql, params, types );	
	}

	@Override
	public List<Project> findAllProjectsSummaryByUser(int userId) {
		//String Query2 = " SELECT `id`, `date_created`, `description`, `project_name`, `user_id`, `farm_id`, `actual_output`, `expected_output`, `unit_id` "
		//		+ " FROM projects WHERE user_id = " + userId;		
		
		String Query = "SELECT  sum( IF(t.transaction_type_id=0, t.amount, 0)) as total_income , sum( IF(t.transaction_type_id=1, t.amount, 0)) as total_expense , p.id as project_id, p.date_created, p.description, p.project_name, p.user_id, p.farm_id, p.actual_output, p.expected_output, p.unit_id " + 
		"FROM `transactions` t, projects p " + 
		"WHERE t.project_id = p.id and p.user_id = " + userId + " group by p.id";
		
		logger.debug("ProjectDaoImpl->findAllProjectsByUser() >>> Query {} ", Query);
		
		List<Project> projects = new ArrayList<Project>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);		
		
		for (Map<String, Object> row : rows) {
			Project project = new Project();
			project.setId((int) row.get("project_id"));
			project.setDateCreated((Date) row.get("date_created"));
			project.setDescription((String) row.get("description"));
			project.setProjectName((String) row.get("project_name"));
			project.setUserId((int) row.get("user_id"));
			project.setFarmId((int) row.get("farm_id"));
			project.setActualOutput((int) row.get("actual_output"));
			
			project.setExpectedOutput((int) row.get("expected_output"));
			project.setUnitId((int) row.get("unit_id"));
			
			project.setTotalExpeses((BigDecimal) row.get("total_expense") );
			project.setTotalIncomes((BigDecimal) row.get("total_income") );
			
			projects.add(project);
		}
		
		return projects;
	}
	
	@Override
	public List<Project> findAllProjectsByUser(int userId) {
		//String Query2 = " SELECT `id`, `date_created`, `description`, `project_name`, `user_id`, `farm_id`, `actual_output`, `expected_output`, `unit_id` "
		//		+ " FROM projects WHERE user_id = " + userId;		
		
		String Query = "SELECT  p.id as project_id, p.date_created, p.description, p.project_name, p.user_id, p.farm_id, p.actual_output, p.expected_output, p.unit_id " + 
		"FROM projects p " + 
		"WHERE p.user_id = " + userId ;
		
		logger.debug("ProjectDaoImpl->findAllProjectsByUser() >>> Query {} ", Query);
		
		List<Project> projects = new ArrayList<Project>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);		
		
		for (Map<String, Object> row : rows) {
			Project project = new Project();
			project.setId((int) row.get("project_id"));
			project.setDateCreated((Date) row.get("date_created"));
			project.setDescription((String) row.get("description"));
			project.setProjectName((String) row.get("project_name"));
			project.setUserId((int) row.get("user_id"));
			project.setFarmId((int) row.get("farm_id"));
			project.setActualOutput((int) row.get("actual_output"));
			
			project.setExpectedOutput((int) row.get("expected_output"));
			project.setUnitId((int) row.get("unit_id"));
			
			projects.add(project);
		}
		
		return projects;
	}

	
}
