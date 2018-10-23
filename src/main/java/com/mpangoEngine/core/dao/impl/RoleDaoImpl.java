package com.mpangoEngine.core.dao.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.RoleDao;
import com.mpangoEngine.core.model.Role;

@Component
public class RoleDaoImpl implements RoleDao {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public List<Role> getUserRoleDetails() {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Role> criteria = builder.createQuery(Role.class);
		Root<Role> contactRoot = criteria.from(Role.class);
		criteria.select(contactRoot);
		List<Role> userRolesdetails = session.createQuery(criteria).getResultList();
		session.close();
		return userRolesdetails;
	}

	public Role findByRole(String name) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Role> criteria = builder.createQuery(Role.class);
		Root<Role> contactRoot = criteria.from(Role.class);
		// ParameterExpression<Integer> p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("name"), name));
		Role role = (Role) session.createQuery(criteria).getSingleResult();
		session.close();
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
