package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.ReportObject;

@Transactional
@Repository
public interface ExpenseDao {

	// public Expense findById(long id);
	// Expense findById(long id);
	// public List<Expense> findAll();

	Expense findById(int id);
	
	Boolean existsById(int id);

	// Expense findByName(String name);

	void save(Expense expense);

	// void updateExpense(Expense expense);

	// void deleteExpenseById(long id);

	List<Expense> findAll();

	// void deleteAllExpenses();

	// boolean isExpenseExist(Expense expense);

	public List<Expense> findAllExpensesByUserId(int userId);
	
	public List<ReportObject> findFinancialExpensesByUserId(int userId);

}
