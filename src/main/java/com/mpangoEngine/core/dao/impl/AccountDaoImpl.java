package com.mpangoEngine.core.dao.impl;

import java.util.ArrayList;
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

import com.mpangoEngine.core.dao.AccountDao;
import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Account;
import com.mpangoEngine.core.model.AccountGroup;

@Component
@Transactional
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {

	public static final Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public AccountDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Override
	public Account findAccountById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> findAllAccountsByUser(int userId) {

		String Query = "SELECT "
				+ "			coa.id, coa.account_name, coa.account_code, coa.account_type_id, coa.user_id, coa.description, "
				+ " 		t.account_type_code, t.account_type_name, "
				+ " 		g.id as account_group_type_id , g.account_group_type_code, g.account_group_type_name "
				+ " FROM " + "			accounts coa, " + "			account_type t, "
				+ "			account_group_types g " + " WHERE " + "			coa.account_type_id = t.id "
				+ "		AND " + "			g.id=t.account_group_type_id  " + "		AND " + "			coa.user_id = "
				+ userId;

		logger.debug("ChartOfAccountsDaoImpl->findAllByUser() >>> Query {} ", Query);
		
		List<Account> accounts = new ArrayList<Account>();


		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {			
			Account acc = new Account();
			
			acc.setId((int) row.get("id"));
			acc.setAccountName((String) row.get("account_name"));
			acc.setAccountCode((String) row.get("account_code"));
			acc.setAccountTypeId((int) row.get("account_type_id"));
			acc.setUserId((int) row.get("user_id"));
			acc.setDescription((String) row.get("description"));
			acc.setAccountTypeCode((String) row.get("account_type_code"));
			acc.setAccountTypeName((String) row.get("account_type_name"));

			acc.setAccountGroupTypeCode((String) row.get("account_group_type_code"));
			acc.setAccountGroupTypeId((int) row.get("account_group_type_id"));			
			acc.setAccountGroupTypeName((String) row.get("account_group_type_name"));

			accounts.add(acc);
		}

		return accounts;
	}

	@Override
	public List<ChartOfAccounts> fetchUserCoa(int userId) {		

		logger.debug("ChartOfAccountsDaoImpl->fetchAllAccountTypes() ");
		
		List<Account> accountsUser = findAllAccountsByUser(userId); //accounts for this user
		
		List<AccountGroup> allAccGroups = getAccountGroups(); // all account group types		
		
		List<ChartOfAccounts> coaArray = new ArrayList<ChartOfAccounts>();
		
		for (AccountGroup grp : allAccGroups) {
			ChartOfAccounts coa = new ChartOfAccounts();
			
			coa.setAccountGroupTypeId(grp.getId());
			coa.setAccountGroupTypeName(grp.getAccountGroupTypeName());
			coa.setAccountGroupTypeCode(grp.getAccountGroupTypeCode());
			
			List<Account> accforgroup =  new ArrayList<Account>();			
			for (Account acc : accountsUser) {
				if(acc.getAccountGroupTypeCode().equals(grp.getAccountGroupTypeCode())) {
					accforgroup.add(acc);
				}
			}			
			coa.setListOfAccounts(accforgroup);
			coaArray.add(coa);
		}
		return coaArray;
	}

	@Override
	public Boolean existsById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(Account coa) {
		String sql = "INSERT INTO accounts "
				+ "( `account_name`, `account_code`, `account_type_id`, `user_id`, `description`) "
				+ "VALUES ( ?, ?, ?, ?, ?)";

		getJdbcTemplate().update(sql, new Object[] { coa.getAccountName(), coa.getAccountCode(), coa.getAccountTypeId(),
				coa.getUserId(), coa.getDescription() });

	}

	@Override
	public List<AccountGroup> getAccountGroups() {
		String Query = "SELECT id, account_group_type_code, account_group_type_name   FROM account_group_types";

		logger.debug("AccountDaoImpl->getAccountGroups() >>> Query {} ", Query);

		List<AccountGroup> accgroupArray = new ArrayList<AccountGroup>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {
			AccountGroup accgroup = new AccountGroup();

			accgroup.setId((int) row.get("id"));
			accgroup.setAccountGroupTypeCode((String) row.get("account_group_type_code"));
			accgroup.setAccountGroupTypeName((String) row.get("account_group_type_name"));

			accgroupArray.add(accgroup);
		}

		return accgroupArray;
	}

	@Override
	public List<Account> findAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

}
