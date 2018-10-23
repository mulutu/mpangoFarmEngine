package com.mpangoEngine.core.service;

import java.util.List;

import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Income;

public interface FinancialsService {
	
	List<Income> getAllIncomes();
	
	List<Expense> getAllExpenses();

}
