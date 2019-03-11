package com.mpangoEngine.core.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.RoleDao;
import com.mpangoEngine.core.model.Role;
import com.mpangoEngine.core.model.Transaction;

@Component
public class RoleDaoImpl extends JdbcDaoSupport implements RoleDao {
	
	public static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public RoleDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public List<Role> getUserRoleDetails() {
		String Query = "SELECT id, name FROM roles"; 

		logger.debug("RoleDaoImpl->getUserRoleDetails() >>> Query {} ", Query);

		List<Role> roles = new ArrayList<Role>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);		

		for (Map<String, Object> row : rows) {
			
			Role role = new Role();	
			
			role.setId((int) row.get("id"));
			role.setName((String) row.get("name"));
			roles.add(role);
		}
		return roles;
	}

	public Role findByRole(String name) {
	
		String Query = "SELECT id, name FROM roles WHERE name = '" +  name + "'"; 

		logger.debug("RoleDaoImpl->findByRole() >>> Query {} ", Query);

		Role role = new Role();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);		

		for (Map<String, Object> row : rows) {			
			role.setId((int) row.get("id"));
			role.setName((String) row.get("name"));
		}
		return role;
	}
	
	@Override
	@Transactional
	public void saveRole(Role role) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		session.beginTransaction();
		//Transaction tx1 = session.beginTransaction();
		session.merge(role);
		session.getTransaction().commit();
		session.close();
		//int id = (int) session.save(user);
		//tx1.commit();
		//session.close();
	}

}
