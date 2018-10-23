package com.mpangoEngine.core.dao.impl;

import java.util.ArrayList;
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

import com.mpangoEngine.core.dao.CustomerDao;
import com.mpangoEngine.core.model.Customer;

@Component
@Transactional
public class CustomerDaoImpl extends JdbcDaoSupport implements CustomerDao {

	public static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public CustomerDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Override
	public Customer findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> findAllCustomersByUserId(int userId) {
		
		String Query = "SELECT id, customer_names, description, user_id FROM customers WHERE user_id = " + userId;

		logger.debug("ExpenseDaoImpl->findAllExpensesByUserId() >>> Query {} ", Query);

		List<Customer> customers = new ArrayList<Customer>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {
			Customer customerObj = new Customer();
			customerObj.setId((int) row.get("id"));
			customerObj.setCustomerNames((String) row.get("customer_names"));
			customerObj.setUserID((int) row.get("user_id"));
			customerObj.setDescription((String) row.get("description"));
			
			customers.add(customerObj);
		}

		return customers;
	}

	@Override
	public boolean existsById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(Customer customer) {
		String sql = "INSERT INTO customers "
				+ "(`user_id`, `description`, `customer_names`) "
				+ "VALUES (?, ?, ?)";

		getJdbcTemplate().update(sql, new Object[] { customer.getUserID(), customer.getDescription(), customer.getCustomerNames() });
	}

}
