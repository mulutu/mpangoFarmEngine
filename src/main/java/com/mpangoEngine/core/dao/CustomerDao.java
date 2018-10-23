package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mpangoEngine.core.model.Customer;

public interface CustomerDao {
	Customer findById(int id);

	List<Customer> findAll();
	
	public List<Customer> findAllCustomersByUserId(int userId);

	boolean existsById(int id);

	void save(Customer customer);
}