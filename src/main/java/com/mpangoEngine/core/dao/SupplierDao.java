package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Customer;
import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Supplier;

@Transactional
@Repository
public interface SupplierDao {
	Supplier findById(long id);

	List<Supplier> findAll();
	
	boolean existsById(int id);
	
	void save(Supplier supplier);
	
	public List<Supplier> findAllSuppliersByUserId(int userId);
}
