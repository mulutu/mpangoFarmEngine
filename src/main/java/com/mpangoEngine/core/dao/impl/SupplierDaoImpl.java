package com.mpangoEngine.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.SupplierDao;
import com.mpangoEngine.core.model.Supplier;


@Component
@Transactional
public class SupplierDaoImpl extends JdbcDaoSupport implements SupplierDao {
	
	public static final Logger logger = LoggerFactory.getLogger(SupplierDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public SupplierDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Override
	public Supplier findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Supplier> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(Supplier supplier) {
		String sql = "INSERT INTO suppliers "
				+ "(`user_id`, `description`, `supplier_names`) "
				+ "VALUES (?, ?, ?)";

		getJdbcTemplate().update(sql, new Object[] { supplier.getUserID(), supplier.getDescription(), supplier.getSupplierNames() });		
	}

	@Override
	public List<Supplier> findAllSuppliersByUserId(int userId) {
		
		String Query = "SELECT id, supplier_names, description, user_id, date_created FROM suppliers WHERE user_id = " + userId;

		logger.debug("SupplierDaoImpl->findAllSuppliersByUserId() >>> Query {} ", Query);

		List<Supplier> suppliers = new ArrayList<Supplier>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {
			Supplier supplierObj = new Supplier();
			supplierObj.setId((int) row.get("id"));
			supplierObj.setSupplierNames((String) row.get("supplier_names"));
			supplierObj.setUserID((int) row.get("user_id"));
			supplierObj.setDescription((String) row.get("description"));
			supplierObj.setDateCreated( (Date) row.get("date_created") );
			
			suppliers.add(supplierObj);
		}

		return suppliers;
		
	}

}
