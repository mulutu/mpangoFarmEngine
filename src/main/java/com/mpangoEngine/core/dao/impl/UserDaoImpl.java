package com.mpangoEngine.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.UserDao;
import com.mpangoEngine.core.model.Customer;
import com.mpangoEngine.core.model.Farm;
import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Role;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.Transactional;

@Component
@Transactional
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	public static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Transactional
	public List getAllUsers() {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(MyUser.class);
		Root contactRoot = criteria.from(MyUser.class);
		criteria.select(contactRoot);
		// session.close();
		return session.createQuery(criteria).getResultList();
	}

	@Transactional
	public List<MyUser> getUserDetails(long userid) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(MyUser.class);
		Root contactRoot = criteria.from(MyUser.class);
		// ParameterExpression p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("id"), userid));
		// session.close();
		return session.createQuery(criteria).getResultList();
	}

	@Override
	@Transactional
	public MyUser findUserByUserName(String username) { // to modify
		String Query = "SELECT * FROM user WHERE username = ?";

		MyUser user = (MyUser) jdbcTemplate.queryForObject(Query, new Object[] { username }, new BeanPropertyRowMapper(MyUser.class));
		
		logger.debug("UserDaoImpl->findUserByUserName() >>> Query {} ", Query);
		
		logger.debug("UserDaoImpl->findUserByUserName() >>> user {} ", user);
		
		return user;
	}
	
	@Override
	@Transactional
	public List<String> getRoleNames(Long userId) {
        String sql = "SELECT * FROM `user_role`, `role` WHERE role.id=user_role.role_id and user_role.user_id= ? ";
 
        Object[] params = new Object[] { userId };
 
        List<String> roles = this.getJdbcTemplate().queryForList(sql, params, String.class);
 
        return roles;
    }

	@Override
	@Transactional
	public void saveUser(MyUser user) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		session.beginTransaction();
		// Transaction tx1 = session.beginTransaction();
		session.merge(user);
		session.getTransaction().commit();
		// session.close();
		// int id = (int) session.save(user);
		// tx1.commit();
		// session.close();
	}

	@Override
	@Transactional
	public MyUser findByConfirmationToken(String confirmationToken) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<MyUser> criteria = builder.createQuery(MyUser.class);
		Root<MyUser> contactRoot = criteria.from(MyUser.class);
		// ParameterExpression<Integer> p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("confirmationToken"), confirmationToken));
		MyUser user_ = (MyUser) session.createQuery(criteria).getSingleResult();
		// session.close();
		return user_;
	}

	@Override
	@Transactional
	public MyUser findUserByEmail(String email) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<MyUser> criteria = builder.createQuery(MyUser.class);
		Root<MyUser> contactRoot = criteria.from(MyUser.class);
		// ParameterExpression<Integer> p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("email"), email));
		MyUser user_ = (MyUser) session.createQuery(criteria).getSingleResult();
		// session.close();
		return user_;
	}
}
