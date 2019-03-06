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

import com.mpangoEngine.core.dao.TransactionDao;
import com.mpangoEngine.core.model.Transaction;

@Component
@Transactional
public class TransactionDaoImpl extends JdbcDaoSupport implements TransactionDao {

	public static final Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public TransactionDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Override
	public Boolean transactionExistsById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction findTransactionById(int id) {
		String Query = "SELECT e.id, e.account_id, transaction_type_id, e.amount, e.project_id, e.transaction_date, e.description, e.payment_method_id, e.user_id, "
				+ "p.project_name, f.farm_name, coa.account_name "
				+ "FROM transactions e, user u, projects p, farm f, chart_of_accounts coa "
				+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and coa.id = e.account_id and e.id = "
				+ id;

		logger.debug("TransactionDaoImpl->findTransactionById() >>> Query {} ", Query);

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		Transaction transaction = new Transaction();

		for (Map<String, Object> row : rows) {
			transaction.setId((int) row.get("id"));
			transaction.setAccountId((int) row.get("account_id"));
			transaction.setAmount((BigDecimal) row.get("amount"));
			transaction.setProjectId((int) row.get("project_id"));
			transaction.setTransactionDate((Date) row.get("transaction_date"));
			transaction.setDescription((String) row.get("description"));
			transaction.setUserId((int) row.get("user_id"));
			transaction.setProjectName((String) row.get("project_name"));
			transaction.setFarmName((String) row.get("farm_name"));
			transaction.setAccountName((String) row.get("account_name"));

			transaction.setTransactionTypeId((int) row.get("transaction_type_id"));

			int trxTypeId = (int) row.get("transaction_type_id");
			if (trxTypeId == 0) {
				transaction.setTransactionType("INCOME");
			} else if (trxTypeId == 1) {
				transaction.setTransactionType("EXPENSE");
			}
		}
		return transaction;
	}

	@Override
	public int saveTransaction(Transaction transaction) {
		String sql = "INSERT INTO transactions "
				+ "(`id`, `account_id`, `transaction_type_id`,`amount`, `transaction_date`, `description`, `user_id`, `project_id`) "
				+ "VALUES (?,?,?,?,?,?,?,?)";

		logger.debug("TransactionDaoImpl->saveTransaction() >>> Transaction {} ", transaction);

		Object[] params = { transaction.getId(), transaction.getAccountId(), transaction.getTransactionTypeId(),
				transaction.getAmount(), transaction.getTransactionDate(), transaction.getDescription(), transaction.getUserId(), transaction.getProjectId() };
		int[] types = { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.DECIMAL, Types.DATE, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER };

		return getJdbcTemplate().update(sql, params, types);
	}

	@Override
	public int updateTransaction(Transaction transaction) {
		String sql = " UPDATE transactions "
				+ " SET account_id=?, transaction_type_id=?, amount=?, transaction_date=?, description=?, user_id=?, project_id=? "
				+ " WHERE id=?";

		logger.debug("TransactionDaoImpl->updateTransaction() >>> transaction {} ", transaction);

		Object[] params = { transaction.getAccountId(), transaction.getTransactionTypeId(), transaction.getAmount(),
				transaction.getTransactionDate(), transaction.getDescription(),
				transaction.getUserId(), transaction.getProjectId(), transaction.getId() };
		int[] types = { Types.INTEGER, Types.INTEGER, Types.DECIMAL, Types.DATE, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER };

		return getJdbcTemplate().update(sql, params, types);
	}

	@Override
	public List<Transaction> findAll() {
		String Query = "SELECT e.id, e.account_id, e.transaction_type_id, e.amount, e.project_id, e.transaction_date, e.description, e.user_id, "
				+ "p.project_name, f.farm_name, coa.account_name "
				+ "FROM transactions e, user u, projects p, farm f, chart_of_accounts coa "
				+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and coa.id = e.account_id ";

		logger.debug("TransactionDaoImpl->findAll() >>> Query {} ", Query);

		List<Transaction> transactions = new ArrayList<Transaction>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		Transaction transaction = new Transaction();

		for (Map<String, Object> row : rows) {
			transaction.setId((int) row.get("id"));
			transaction.setAccountId((int) row.get("account_id"));
			transaction.setTransactionTypeId((int) row.get("transaction_type_id"));
			transaction.setAmount((BigDecimal) row.get("amount"));
			transaction.setProjectId((int) row.get("project_id"));
			transaction.setTransactionDate((Date) row.get("transaction_date"));
			transaction.setDescription((String) row.get("description"));
			transaction.setUserId((int) row.get("user_id"));
			transaction.setProjectName((String) row.get("project_name"));
			transaction.setFarmName((String) row.get("farm_name"));
			transaction.setAccountName((String) row.get("account_name"));

			transaction.setTransactionTypeId((int) row.get("transaction_type_id"));

			int trxTypeId = (int) row.get("transaction_type_id");
			if (trxTypeId == 0) {
				transaction.setTransactionType("INCOME");
			} else if (trxTypeId == 1) {
				transaction.setTransactionType("EXPENSE");
			}

			transactions.add(transaction);
		}
		return transactions;
	}

	@Override
	public List<Transaction> findUserTransactions(int userId) {		
		String Query = "SELECT e.id, e.account_id, e.transaction_type_id, e.amount, e.project_id, e.transaction_date, e.description, e.user_id, p.project_name, f.id, f.farm_name, coa.account_name " 
						+ " FROM transactions e, user u, projects p, farm f, chart_of_accounts coa "
						+ "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and coa.id = e.account_id and e.user_id = " + userId; 

		logger.debug("TransactionDaoImpl->findUserTransactions() >>> Query {} ", Query);

		List<Transaction> transactions = new ArrayList<Transaction>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);		

		for (Map<String, Object> row : rows) {
			Transaction transaction = new Transaction();
			transaction.setId((int) row.get("id"));
			transaction.setAccountId((int) row.get("account_id"));
			transaction.setTransactionTypeId((int) row.get("transaction_type_id"));
			transaction.setAmount((BigDecimal) row.get("amount"));
			transaction.setProjectId((int) row.get("project_id"));
			transaction.setTransactionDate((Date) row.get("transaction_date"));
			transaction.setDescription((String) row.get("description"));
			transaction.setUserId((int) row.get("user_id"));
			transaction.setProjectName((String) row.get("project_name"));
			transaction.setFarmName((String) row.get("farm_name"));
			transaction.setAccountName((String) row.get("account_name"));

			transaction.setTransactionTypeId((int) row.get("transaction_type_id"));

			int trxTypeId = (int) row.get("transaction_type_id");
			if (trxTypeId == 0) {
				transaction.setTransactionType("INCOME");
			} else if (trxTypeId == 1) {
				transaction.setTransactionType("EXPENSE");
			}

			transactions.add(transaction);
		}
		return transactions;
	}

}
