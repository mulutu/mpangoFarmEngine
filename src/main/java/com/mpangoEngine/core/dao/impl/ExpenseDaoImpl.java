package com.mpangoEngine.core.dao.impl;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.ExpenseDao;
import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Income;
import com.mpangoEngine.core.model.ReportObject;

@Component
@Transactional
public class ExpenseDaoImpl extends JdbcDaoSupport implements ExpenseDao {

	public static final Logger logger = LoggerFactory.getLogger(ExpenseDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	public ExpenseDaoImpl(DataSource dataSource) {
	    super();
	    setDataSource(dataSource);
	}

	@Transactional
	public List<ReportObject> findFinancialExpensesByUserId(int userId) {

		String query = "SELECT coa.id as id, sum(i.amount) as TotalAmount, coa.account_name as Description "
				+ "FROM expense as i, chart_of_accounts as coa " + "WHERE i.user_id =" + userId
				+ " and coa.id = i.account_id " + "GROUP BY coa.id, coa.account_name";

		List<ReportObject> expenses = new ArrayList<ReportObject>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> row : rows) {
			ReportObject expenseObj = new ReportObject();
			expenseObj.setId((int) row.get("id"));
			expenseObj.setTotalAmount((BigDecimal) row.get("TotalAmount"));
			expenseObj.setDescription((String) row.get("Description"));
			expenses.add(expenseObj);
		}
		return expenses;
	}

	@Override
	public Expense findById(int id) {
		String query = "SELECT * FROM expense WHERE id = ?";
		Expense expense = (Expense) jdbcTemplate.queryForObject(query, new Object[] { id }, new BeanPropertyRowMapper(Expense.class));
		return expense;
	}

	@Override
	public List<Expense> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Expense> findAllExpensesByUserId(int userId) {
		String Query = "SELECT e.id, e.account_id,e.amount, e.project_id, e.expense_date, e.notes, e.payment_method_id, e.supplier_id, e.user_id, "
				+ "p.project_name, f.farm_name, pm.payment_method, coa.account_name, c.Supplier_Names "
				+ "FROM expense e, user u, project p, farm f, payment_method pm, chart_of_accounts coa, suppliers c "
				+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and e.payment_method_id = pm.id and coa.id = e.account_id and c.id = e.supplier_id and u.id = "
				+ userId;

		logger.debug("ExpenseDaoImpl->findAllExpensesByUserId() >>> Query {} ", Query);

		List<Expense> expenses = new ArrayList<Expense>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {
			Expense expenseObj = new Expense();
			expenseObj.setId((int) row.get("id"));
			expenseObj.setAccountId((int) row.get("account_id"));
			expenseObj.setAmount((BigDecimal) row.get("amount"));
			expenseObj.setProjectId((int) row.get("project_id"));
			expenseObj.setExpenseDate((Date) row.get("expense_date"));
			expenseObj.setNotes((String) row.get("notes"));
			expenseObj.setPaymentMethodId((int) row.get("payment_method_id"));
			expenseObj.setSupplierId((int) row.get("supplier_id"));
			expenseObj.setUserId((int) row.get("user_id"));

			expenseObj.setProjectName((String) row.get("project_name"));
			expenseObj.setFarmName((String) row.get("farm_name"));
			expenseObj.setPaymentMethod((String) row.get("payment_method"));
			expenseObj.setAccount((String) row.get("account_name"));
			expenseObj.setSupplier((String) row.get("Supplier_Names"));

			expenses.add(expenseObj);
		}
		return expenses;
	}

	@Override
	public int save(Expense expense) {
		String sql = "INSERT INTO expense "
				+ "(`id`, `account_id`, `amount`, `expense_date`, `notes`, `payment_method_id`, `supplier_id`, `user_id`, `project_id`) "
				+ "VALUES (?, ?, ?, ?, ?, ? ,? , ?, ?)";
		
		logger.debug("ExpenseDaoImpl->save() >>> getId {} ", expense.getId());
		logger.debug("ExpenseDaoImpl->save() >>> getAccountId {} ", expense.getAccountId());
		logger.debug("ExpenseDaoImpl->save() >>> getAmount {} ", expense.getAmount());
		logger.debug("ExpenseDaoImpl->save() >>> getExpenseDate {} ", expense.getExpenseDate());
		logger.debug("ExpenseDaoImpl->save() >>> getNotes {} ", expense.getNotes());
		logger.debug("ExpenseDaoImpl->save() >>> getPaymentMethodId {} ", expense.getPaymentMethodId());
		logger.debug("ExpenseDaoImpl->save() >>> getSupplierId {} ", expense.getSupplierId());
		logger.debug("ExpenseDaoImpl->save() >>> getUserId {} ", expense.getUserId());
		logger.debug("ExpenseDaoImpl->save() >>> getProjectId {} ", expense.getProjectId() );
		
		Object[] params = {expense.getId(), expense.getAccountId(), expense.getAmount(), expense.getExpenseDate(),
				expense.getNotes(), expense.getPaymentMethodId(), expense.getSupplierId(), expense.getUserId(),
				expense.getProjectId()};
		int[] types = {Types.INTEGER, Types.INTEGER, Types.DECIMAL, Types.DATE, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };

		return getJdbcTemplate().update( sql, params, types );	

	}
	
	@Override
	public int updateExpense(Expense expense) {
		String sql = " UPDATE expense "
				+ " SET account_id=?, amount=?, expense_date=?, notes=?, payment_method_id=?, supplier_id=?, user_id=?, project_id=? "
				+ " WHERE id=?";

		logger.debug("ExpenseDaoImpl->updateExpense() >>> Query {} ", sql);
		logger.debug("ExpenseDaoImpl->updateExpense() >>> income.getId() {} ", expense.getId());

		//Object[] params = { name, id};		  
		//int[] types = {Types.INT, Types.DECIMAL(19,2), Types.INT, Types.DATETIME, Types.VARCHAR, Types.INT, Types.INT, Types.INT};    Types.VARCHAR, Types.BIGINT};
		//int rows = template.update(updateSql, params, types);
		
		Object[] params = {expense.getAccountId(), expense.getAmount(), expense.getExpenseDate(), expense.getNotes(), expense.getPaymentMethodId(), expense.getSupplierId(), expense.getUserId(), expense.getProjectId(), expense.getId() };
		int[] types = {Types.INTEGER, Types.DECIMAL, Types.DATE, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER}; 

		return getJdbcTemplate().update(sql, params, types);
		
		//this.getJdbcTemplate().update(sql,
			//	new Object[] { income.getAccountId(), income.getAmount(), income.getIncomeDate(),
				//		income.getNotes(), income.getPaymentMethodId(), income.getCustomerId(), income.getUserId(),
				//		income.getProjectId(), income.getId() });		
	}

	@Override
	public Boolean existsById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

}
