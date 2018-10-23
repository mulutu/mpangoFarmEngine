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

import com.mpangoEngine.core.dao.ChartOfAccountsDao;
import com.mpangoEngine.core.model.COAAccountType;
import com.mpangoEngine.core.model.ChartOfAccounts;


@Component
@Transactional
public class ChartOfAccountsDaoImpl extends JdbcDaoSupport implements ChartOfAccountsDao {
	
	public static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public ChartOfAccountsDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Override
	public ChartOfAccounts findAccountById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChartOfAccounts> findAllCOAByUser(int userId) {
		
		String Query = "SELECT coa.id, coa.account_name, coa.account_code, coa.account_type_id, coa.user_id, coa.description, "
				+ " t.account_type_code, t.account_type_name, "
				+ " g.account_group_type_code, g.id as account_group_type_id , g.account_group_type_name "
				+ " FROM chart_of_accounts coa, coa_account_type t, account_group_types g "
				+ " WHERE coa.account_type_id = t.id AND g.id=t.account_group_type_id  AND coa.user_id = "  + userId;
		
		logger.debug("ChartOfAccountsDaoImpl->findAllByUser() >>> Query {} ", Query);

		List<ChartOfAccounts> coa = new ArrayList<ChartOfAccounts>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		for (Map<String, Object> row : rows) {
			ChartOfAccounts coaObj = new ChartOfAccounts();
			coaObj.setId((int) row.get("id"));
			coaObj.setAccountName((String) row.get("account_name"));
			coaObj.setAccountCode((String) row.get("account_code"));
			coaObj.setAccountTypeId((int) row.get("account_type_id"));
			coaObj.setUserId((int) row.get("user_id"));
			coaObj.setDescription((String) row.get("description"));
			coaObj.setAccountTypeCode((String) row.get("account_type_code"));
			coaObj.setAccountTypeName((String) row.get("account_type_name"));
			
			coaObj.setAccountGroupTypeCode((String) row.get("account_group_type_code"));
			coaObj.setAccountGroupTypeId( (int) row.get("account_group_type_id"));
			coaObj.setAccountGroupTypeName((String) row.get("account_group_type_name"));
			
			coa.add(coaObj);
		}

		return coa;
	}

	@Override
	public List<ChartOfAccounts> findAllCOA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<COAAccountType> fetchAllAccountTypes() {
		String Query = "SELECT * FROM coa_account_type";
		
		logger.debug("ChartOfAccountsDaoImpl->fetchAllAccountTypes() >>> Query {} ", Query);

		List<COAAccountType> type = new ArrayList<COAAccountType>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);
		
		for (Map<String, Object> row : rows) {
			
			COAAccountType coaObj = new COAAccountType();
			
			coaObj.setId((int) row.get("id"));
			coaObj.setAccountTypeName((String) row.get("account_type_name"));
			coaObj.setAccountTypeCode((String) row.get("account_type_code"));
			coaObj.setAccountGroupTypeId((int) row.get("account_group_type_id"));
			
			type.add(coaObj);
		}
		
		return type;
	}

	@Override
	public Boolean existsById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(ChartOfAccounts coa) {
		String sql = "INSERT INTO chart_of_accounts "
				+ "( `account_name`, `account_code`, `account_type_id`, `user_id`, `description`) "
				+ "VALUES ( ?, ?, ?, ?, ?)";

		getJdbcTemplate().update(sql,
				new Object[] {  coa.getAccountName(), coa.getAccountCode(), coa.getAccountTypeId(), coa.getUserId(), coa.getDescription() });
		
	}
	
}
