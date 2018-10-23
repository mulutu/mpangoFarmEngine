package com.mpangoEngine.core.dao.impl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.PrivilegeDao;
import com.mpangoEngine.core.model.Privilege;

@Component
public class PrivilegeDaoImpl implements PrivilegeDao {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public Privilege findPrivilegeByName(String name) {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Privilege> criteria = builder.createQuery(Privilege.class);
		Root<Privilege> contactRoot = criteria.from(Privilege.class);
		// ParameterExpression<Integer> p = builder.parameter(Integer.class);
		criteria.select(contactRoot).where(builder.equal(contactRoot.get("name"), name));
		Privilege privilege = (Privilege) session.createQuery(criteria).getSingleResult();
		session.close();
		return privilege;
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
