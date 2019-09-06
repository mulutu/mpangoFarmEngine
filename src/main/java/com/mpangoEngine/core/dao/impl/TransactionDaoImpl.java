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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

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
                + "FROM transactions e, users u, projects p, farm f, accounts coa "
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

        int response = 1;

        logger.debug("TransactionDaoImpl->saveTransaction() >>> Transaction {} ", transaction);
        logger.debug("TransactionDaoImpl->saveTransaction() >>> Transaction Selected Projects {} ", transaction.getSelectedProjectIds());

        String Query = "INSERT INTO transactions "
                + "(`id`, `account_id`, `transaction_type_id`,`amount`, `transaction_date`, `description`, `user_id`, `project_id`) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        List<Integer> selectedProjectIds = transaction.getSelectedProjectIds();
        int numberOftransactions = selectedProjectIds.size();

        BigDecimal amtFull = transaction.getAmount();

        BigDecimal allocatedAmt = amtFull.divide(new BigDecimal(numberOftransactions));

        for (Integer projectId : selectedProjectIds) {
            logger.debug("TransactionDaoImpl->saveTransaction() >>> Transaction Selected Project ID: {} ", projectId);
            Object[] params = {transaction.getId(), transaction.getAccountId(), transaction.getTransactionTypeId(), allocatedAmt, transaction.getTransactionDate(), transaction.getDescription(), transaction.getUserId(), projectId};
            int[] types = {Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.DECIMAL, Types.DATE, Types.VARCHAR, Types.INTEGER, Types.INTEGER};

            getJdbcTemplate().update(Query, params, types);

            logger.debug("TransactionDaoImpl->updateTransaction() >>> Query {} ", Query);
        }

        return response;
    }

    /*public void insertListOfPojos(final List<Integer> projectIdList, String sql) {
    
        https://www.tutorialspoint.com/springjdbc/springjdbc_batch_operation.htm
        http://www.java2s.com/Code/Java/Spring/UseBatchPreparedStatementSetter.htm

        //String sql = "INSERT INTO MY_TABLE (FIELD_1,FIELD_2,FIELD_3) VALUES (?,?,?)";

        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {

                Integer projId = projectIdList.get(i);
                ps.setInt(1, projId);
                ps.setString(2, projId);
                ps.setString(3, projId);

            }

            @Override
            public int getBatchSize() {
                return projectIdList.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

    }*/
    @Override
    public int updateTransaction(Transaction transaction) {

        logger.debug("TransactionDaoImpl->updateTransaction() >>> transaction {} ", transaction);

        String Query = " UPDATE transactions "
                + " SET account_id=?, transaction_type_id=?, amount=?, transaction_date=?, description=?, project_id=? "
                + " WHERE id=?";

        logger.debug("TransactionDaoImpl->updateTransaction() >>> Query {} ", Query);

        Object[] params = {transaction.getAccountId(), transaction.getTransactionTypeId(), transaction.getAmount(),
            transaction.getTransactionDate(), transaction.getDescription(),
            transaction.getProjectId(), transaction.getId()};

        int[] types = {Types.INTEGER, Types.INTEGER, Types.DECIMAL, Types.DATE, Types.VARCHAR, Types.INTEGER, Types.INTEGER};

        return getJdbcTemplate().update(Query, params, types);
    }

    @Override
    public List<Transaction> findAll() {
        String Query = "SELECT e.id, e.account_id, e.transaction_type_id, e.amount, e.project_id, e.transaction_date, e.description, e.user_id, "
                + "p.project_name, f.farm_name, coa.account_name "
                + "FROM transactions e, users u, projects p, farm f, accounts coa "
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
        String Query = "SELECT e.id as transaction_id, e.account_id, e.transaction_type_id, e.amount, e.project_id, e.transaction_date, e.description, e.user_id, "
                + "p.project_name, f.id as farm_id, f.farm_name, coa.account_name "
                + " FROM transactions e, users u, projects p, farm f, accounts coa "
                + "WHERE u.id=e.user_id and p.id = e.project_id and p.farm_id = f.id and coa.id = e.account_id and e.user_id = " + userId;

        logger.debug("TransactionDaoImpl->findUserTransactions() >>> Query {} ", Query);

        List<Transaction> transactions = new ArrayList<Transaction>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

        for (Map<String, Object> row : rows) {

            Transaction transaction = new Transaction();

            transaction.setId((int) row.get("transaction_id"));
            transaction.setAccountId((int) row.get("account_id"));
            transaction.setTransactionTypeId((int) row.get("transaction_type_id"));
            transaction.setAmount((BigDecimal) row.get("amount"));
            transaction.setProjectId((int) row.get("project_id"));
            transaction.setTransactionDate((Date) row.get("transaction_date"));
            transaction.setDescription((String) row.get("description"));
            transaction.setUserId((int) row.get("user_id"));
            transaction.setProjectName((String) row.get("project_name"));
            transaction.setFarmId((int) row.get("farm_id"));
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
