package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.PaymentMethod;
import com.mpangoEngine.core.model.Project;

@Transactional
@Repository
public interface PaymentMethodDao extends CrudRepository<PaymentMethod, Long> {
	PaymentMethod findById(long id);

	List<PaymentMethod> findAll();
}
