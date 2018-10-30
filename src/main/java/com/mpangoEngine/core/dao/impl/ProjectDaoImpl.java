package com.mpangoEngine.core.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.ProjectDao;
import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Income;
import com.mpangoEngine.core.model.Project;

@Component
@Transactional
public class ProjectDaoImpl extends JdbcDaoSupport implements ProjectDao {

	public static final Logger logger = LoggerFactory.getLogger(ProjectDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	public ProjectDaoImpl(DataSource dataSource) {
	    super();
	    setDataSource(dataSource);
	}

	@Override
	public List<Map<String, Object>> findProjectDetails(int projid) {
		String query = "SELECT p.id, p.date_created, p.description as 'project_desc', p.project_name, p.user_id, p.farm_id, p.actual_output, p.expected_output, u.unit_description,  "
				+ "f.farm_name, f.description "
				+ "FROM project p, farm f , unit_of_output u  " 
				+ "WHERE f.id=p.farm_id  AND p.unit_id = u.id AND  p.id = " + projid;
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		return rows;
	}

	@Override
	public Project findProjectById(int id) {
		String query = "SELECT * FROM project WHERE id = ?";
		Project project = (Project) jdbcTemplate.queryForObject(query, new Object[] { id }, new BeanPropertyRowMapper(Project.class));
		return project;
	}

	@Override
	public List<Project> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> findAllProjectsByUser(int userId) {
		String query = "SELECT p.id, p.date_created, p.description as 'project_desc', p.project_name, p.user_id, p.farm_id, p.actual_output, p.expected_output, p.unit_id, u.unit_description,  "
				+ "f.farm_name, f.description FROM project p, farm f, unit_of_output u " + "where f.id=p.farm_id AND u.id= p.unit_id and  p.user_id = "
				+ userId;

		List<Project> projects = new ArrayList<Project>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> row : rows) {

			Project projectObj = new Project();

			projectObj.setId((int) row.get("id"));
			projectObj.setUserId((int) row.get("user_id"));
			projectObj.setFarmId((int) row.get("farm_id"));
			projectObj.setUnitId((int) row.get("unit_id"));
			projectObj.setUnitDescription((String) row.get("unit_description"));
			projectObj.setActualOutput( (int) row.get("actual_output"));
			projectObj.setExpectedOutput((int) row.get("expected_output"));
			projectObj.setDateCreated((Date) row.get("date_created"));
			projectObj.setProjectName((String) row.get("project_name"));
			projectObj.setDescription((String) row.get("project_desc"));

			projects.add(projectObj);
		}

		return projects;
	}

	@Override
	public boolean existsById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(Project project) {
		String sql = "INSERT INTO income "
				+ "(`id`, `date_created`, `description`, `project_name`, `user_id`, `farm_id`, `actual_output`, `expected_output`, `unit_id`) "
				+ "VALUES (?, ?, ?, ?, ?, ? ,? , ?, ?)";

		getJdbcTemplate().update(sql,
				new Object[] { project.getId(), project.getDateCreated(), project.getDescription(), project.getProjectName(),
						project.getUserId(), project.getFarmId(), project.getActualOutput(), project.getExpectedOutput(),
						project.getUnitId() });	
	}

	@Override
	public List<Income> findAllIncomesByProject(int projid) {
		String Query = "SELECT e.id, e.account_id,e.amount, e.project_id, e.income_date, e.notes, e.payment_method_id, e.customer_id, e.user_id, "
				+ "p.project_name, f.farm_name, pm.payment_method, coa.account_name, c.customer_names "
				+ "FROM income e, user u, project p, farm f, payment_method pm, chart_of_accounts coa, customers c "
				+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and e.payment_method_id = pm.id and coa.id = e.account_id and c.id = e.customer_id and e.project_id = "
				+ projid;

		logger.debug("ProjectDaoImpl->findAllIncomesByProject() >>> Query {} ", Query);

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
			incomeObj.setCustomer((String) row.get("supplier_names"));

			incomes.add(incomeObj);
		}
		return incomes;
	}

	@Override
	public List<Expense> findAllExpensesByProject(int projid) {
		String Query = "SELECT e.id, e.account_id,e.amount, e.project_id, e.expense_date, e.notes, e.payment_method_id, e.supplier_id, e.user_id, "
				+ "p.project_name, f.farm_name, pm.payment_method, coa.account_name, c.supplier_names "
				+ "FROM expense e, user u, project p, farm f, payment_method pm, chart_of_accounts coa, suppliers c "
				+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and e.payment_method_id = pm.id and coa.id = e.account_id and c.id = e.supplier_id and e.project_id = "
				+ projid;

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
			expenseObj.setSupplier((String) row.get("supplier_names"));

			expenses.add(expenseObj);
		}
		return expenses;
	}

	


}