package com.mpangoEngine.core.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.UserDao;
import com.mpangoEngine.core.model.MyUser;

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

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
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
		//session.close();
		return session.createQuery(criteria).getResultList();
	}

	@Transactional
	public List<MyUser> getUserDetails(long userid) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();		
		CriteriaQuery criteria = builder.createQuery(MyUser.class);
		Root contactRoot = criteria.from(MyUser.class);
		//ParameterExpression p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("id"), userid));
		//session.close();
		return session.createQuery(criteria).getResultList();
	}

	@Override
	@Transactional
	public MyUser findUserByUserName(String username) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();		
		CriteriaQuery<MyUser> criteria = builder.createQuery(MyUser.class);
		Root<MyUser> contactRoot = criteria.from(MyUser.class);
		//ParameterExpression<Integer> p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("username"), username));
		MyUser user_ = (MyUser) session.createQuery(criteria).getSingleResult();
		//session.close();
		return user_;
	}

	@Override
	@Transactional
	public void saveUser(MyUser user) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		session.beginTransaction();
		//Transaction tx1 = session.beginTransaction();
		session.merge(user);
		session.getTransaction().commit();
		//session.close();
		//int id = (int) session.save(user);
		//tx1.commit();
		//session.close();
	}

	@Override
	@Transactional
	public MyUser findByConfirmationToken(String confirmationToken) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();		
		CriteriaQuery<MyUser> criteria = builder.createQuery(MyUser.class);
		Root<MyUser> contactRoot = criteria.from(MyUser.class);
		//ParameterExpression<Integer> p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("confirmationToken"), confirmationToken));
		MyUser user_ = (MyUser) session.createQuery(criteria).getSingleResult();
		//session.close();
		return user_;
	}

	@Override
	@Transactional
	public MyUser findUserByEmail(String email) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();		
		CriteriaQuery<MyUser> criteria = builder.createQuery(MyUser.class);
		Root<MyUser> contactRoot = criteria.from(MyUser.class);
		//ParameterExpression<Integer> p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("email"), email));
		MyUser user_ = (MyUser) session.createQuery(criteria).getSingleResult();
		//session.close();
		return user_;
	}
}
