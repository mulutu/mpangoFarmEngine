package com.mpangoEngine.core.dao.impl;

import java.math.BigDecimal;
import java.sql.Types;
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

import com.mpangoEngine.core.dao.IncomeDao;
import com.mpangoEngine.core.model.Income;
import com.mpangoEngine.core.model.ReportObject;

@Component
@Transactional
public class IncomeDaoImpl extends JdbcDaoSupport implements IncomeDao {

	public static final Logger logger = LoggerFactory.getLogger(IncomeDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	public IncomeDaoImpl(DataSource dataSource) {
	    super();
	    setDataSource(dataSource);
	}

	@Transactional
	@Override
	public List<ReportObject> findFinancialIncomesByUserId(int userId) {

		String query = "SELECT coa.id as id, sum(i.amount) as TotalAmount, coa.account_name as Description "
				+ "FROM income as i, chart_of_accounts as coa " + "WHERE i.user_id =" + userId
				+ " and coa.id = i.account_id " + "GROUP BY coa.id, coa.account_name";
		
		logger.debug("IncomeDaoImpl->findFinancialIncomesByUserId() >>> Query {} ", query);

		List<ReportObject> incomes = new ArrayList<ReportObject>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> row : rows) {
			ReportObject incomeObj = new ReportObject();
			
			incomeObj.setId((int) row.get("id"));
			incomeObj.setTotalAmount((BigDecimal) row.get("TotalAmount"));
			incomeObj.setDescription((String) row.get("Description"));
			
			incomes.add(incomeObj);
		}
		return incomes;
	}

	@Override
	public Income findById(int id) {
		
		String Query = "SELECT e.id, e.account_id,e.amount, e.project_id, e.income_date, e.notes, e.payment_method_id, e.customer_id, e.user_id, "
				+ "p.project_name, f.farm_name, pm.payment_method, coa.account_name, c.Customer_Names "
				+ "FROM income e, user u, project p, farm f, payment_method pm, chart_of_accounts coa, customers c "
				+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and e.payment_method_id = pm.id and coa.id = e.account_id and c.id = e.customer_id and e.id = "
				+ id;

		logger.debug("IncomeDaoImpl->findAllIncomesByUserId() >>> Query {} ", Query);
		
		//Income income = (Income) jdbcTemplate.queryForObject(Query, new Object[] { id }, new BeanPropertyRowMapper(Income.class));
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);
		
		Income incomeObj = new Income();
		
		for (Map<String, Object> row : rows) {
			
			incomeObj.setId((int) row.get("id"));
			incomeObj.setAccountId((int) row.get("account_id"));
			incomeObj.setAmount((BigDecimal) row.get("amount"));
			incomeObj.setProjectId((int) row.get("project_id"));
			incomeObj.setIncomeDate((Date) row.get("income_date"));
			incomeObj.setNotes((String) row.get("notes"));
			incomeObj.setPaymentMethodId((int) row.get("payment_method_id"));
			incomeObj.setCustomerId((int) row.get("customer_id"));
			incomeObj.setUserId((int) row.get("user_id"));

			incomeObj.setProjectName((String) row.get("project_name"));
			incomeObj.setFarmName((String) row.get("farm_name"));
			incomeObj.setPaymentMethod((String) row.get("payment_method"));
			incomeObj.setAccount((String) row.get("account_name"));
			incomeObj.setCustomer((String) row.get("Customer_Names"));

			//incomes.add(incomeObj);
		}

		
		return incomeObj;
	}

	@Override
	public List<Income> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Income> findAllIncomesByUserId(int userId) {
		String Query = "SELECT e.id, e.account_id,e.amount, e.project_id, e.income_date, e.notes, e.payment_method_id, e.customer_id, e.user_id, "
				+ "p.project_name, f.farm_name, pm.payment_method, coa.account_name, c.Customer_Names "
				+ "FROM income e, user u, project p, farm f, payment_method pm, chart_of_accounts coa, customers c "
				+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and e.payment_method_id = pm.id and coa.id = e.account_id and c.id = e.customer_id and u.id = "
				+ userId;

		logger.debug("IncomeDaoImpl->findAllIncomesByUserId() >>> Query {} ", Query);

		List<Income> incomes = new ArrayList<Income>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {
			Income incomeObj = new Income();
			incomeObj.setId((int) row.get("id"));
			incomeObj.setAccountId((int) row.get("account_id"));
			incomeObj.setAmount((BigDecimal) row.get("amount"));
			incomeObj.setProjectId((int) row.get("project_id"));
			incomeObj.setIncomeDate((Date) row.get("income_date"));
			incomeObj.setNotes((String) row.get("notes"));
			incomeObj.setPaymentMethodId((int) row.get("payment_method_id"));
			incomeObj.setCustomerId((int) row.get("customer_id"));
			incomeObj.setUserId((int) row.get("user_id"));

			incomeObj.setProjectName((String) row.get("project_name"));
			incomeObj.setFarmName((String) row.get("farm_name"));
			incomeObj.setPaymentMethod((String) row.get("payment_method"));
			incomeObj.setAccount((String) row.get("account_name"));
			incomeObj.setCustomer((String) row.get("Customer_Names"));

			incomes.add(incomeObj);
		}
		return incomes;
	}

	@Override
	public int save(Income income) {
		String sql = "INSERT INTO income "
				+ "(`id`, `account_id`, `amount`, `income_date`, `notes`, `payment_method_id`, `customer_id`, `user_id`, `project_id`) "
				+ "VALUES (?, ?, ?, ?, ?, ? ,? , ?, ?)";
		
		logger.debug("IncomeDaoImpl->save() >>> getId {} ", income.getId());
		logger.debug("IncomeDaoImpl->save() >>> getAccountId {} ", income.getAccountId());
		logger.debug("IncomeDaoImpl->save() >>> getAmount {} ", income.getAmount());
		logger.debug("IncomeDaoImpl->save() >>> getExpenseDate {} ", income.getIncomeDate());
		logger.debug("IncomeDaoImpl->save() >>> getNotes {} ", income.getNotes());
		logger.debug("IncomeDaoImpl->save() >>> getPaymentMethodId {} ", income.getPaymentMethodId());
		logger.debug("IncomeDaoImpl->save() >>> getSupplierId {} ", income.getCustomerId());
		logger.debug("IncomeDaoImpl->save() >>> getUserId {} ", income.getUserId());
		logger.debug("IncomeDaoImpl->save() >>> getProjectId {} ", income.getProjectId() );
		
		Object[] params = {income.getId(), income.getAccountId(), income.getAmount(), income.getIncomeDate(),
				income.getNotes(), income.getPaymentMethodId(), income.getCustomerId(), income.getUserId(),
				income.getProjectId()};
		int[] types = {Types.INTEGER, Types.INTEGER, Types.DECIMAL, Types.DATE, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };
		
		return getJdbcTemplate().update( sql, params, types );

	}
	
	@Override
	public int updateIncome(Income income) {
		String sql = " UPDATE income "
				+ " SET account_id=?, amount=?, income_date=?, notes=?, payment_method_id=?, customer_id=?, user_id=?, project_id=? "
				+ " WHERE id=?";

		logger.debug("IncomeDaoImpl->updateIncome() >>> Query {} ", sql);
		logger.debug("IncomeDaoImpl->updateIncome() >>> income.getId() {} ", income.getId());

		//Object[] params = { name, id};		  
		//int[] types = {Types.INT, Types.DECIMAL(19,2), Types.INT, Types.DATETIME, Types.VARCHAR, Types.INT, Types.INT, Types.INT};    Types.VARCHAR, Types.BIGINT};
		//int rows = template.update(updateSql, params, types);
		
		Object[] params = {income.getAccountId(), income.getAmount(), income.getIncomeDate(), income.getNotes(), income.getPaymentMethodId(), income.getCustomerId(), income.getUserId(), income.getProjectId(), income.getId() };
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
