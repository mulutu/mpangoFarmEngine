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
public interface IncomeDaoWORKING extends CrudRepository<Income, Long> {

	// public Expense findById(long id);
	// Expense findById(long id);
	// public List<Expense> findAll();

	Income findById(long id);

	// Expense findByName(String name);

	// void Save(Expense expense);

	// void updateExpense(Expense expense);

	// void deleteExpenseById(long id);

	List<Income> findAll();

	// void deleteAllExpenses();

	// boolean isExpenseExist(Expense expense);

	@Query(value = "SELECT e.id, e.account_id,	e.amount,	e.project_id, e.income_date, e.notes, "
			+ "e.payment_method_id,	e.customer_id,	e.user_id " 
			+ "from income e, user u "
			+ "where u.id=e.user_id and u.id =?1 ", nativeQuery = true)
	public List<Income> findAllIncomesByUserId(long userId);
	
	
	@Query(value = "SELECT coa.id as id, sum(i.amount) as TotalAmount, coa.account_name as Decsription "
			+ "from income as i, chart_of_accounts as coa "
			+ "where i.user_id =?1 and coa.id = i.account_id "
			+ "group by coa.id, coa.account_name", nativeQuery = true)
	public List<ReportObject> findFinancialIncomesByUserId(long userId);
	
	
	@Query(value = "SELECT firstname, lastname FROM user WHERE id = ?1", nativeQuery = true)
	NameOnly findByNativeQuery(long userId);

	public static interface NameOnly {
		String getFirstname();
		String getLastname();
	}

}
