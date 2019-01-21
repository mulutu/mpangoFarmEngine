package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Income;
import com.mpangoEngine.core.model.ReportObject;

@Transactional
@Repository
public interface IncomeDao{

	// public Expense findById(long id);
	// Expense findById(long id);
	// public List<Expense> findAll();
	
	Boolean existsById(int id);

	Income findById(int id);

	// Expense findByName(String name);

	int save(Income income);
	
	void updateIncome(Income income);

	// void updateExpense(Expense expense);

	// void deleteExpenseById(long id);

	List<Income> findAll();

	// void deleteAllExpenses();

	// boolean isExpenseExist(Expense expense);


	public List<Income> findAllIncomesByUserId(int userId);
	
	
	public List<ReportObject> findFinancialIncomesByUserId(int userId);

}
