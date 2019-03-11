package com.mpangoEngine.core.dao.impl;

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

import com.mpangoEngine.core.dao.PrivilegeDao;
import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Privilege;

@Component
public class PrivilegeDaoImpl extends JdbcDaoSupport implements PrivilegeDao {
	
	public static final Logger logger = LoggerFactory.getLogger(PrivilegeDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PrivilegeDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Override
	public Privilege findPrivilegeByName(String name) {		
		
		String Query = "SELECT id,name FROM privileges WHERE name = '" + name + "'";

		logger.debug("PrivilegeDaoImpl->findPrivilegeByName() >>> Query {} ", Query);

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		Privilege pri = new Privilege();

		for (Map<String, Object> row : rows) {
			pri.setId((int) row.get("id"));
			pri.setName((String) row.get("name"));
		}		
		return pri;
	}

	@Override
	@Transactional
	public void savePrivilege(Privilege privilege) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		session.beginTransaction();
		session.merge(privilege);
		session.getTransaction().commit();
		session.close();
	}

}
