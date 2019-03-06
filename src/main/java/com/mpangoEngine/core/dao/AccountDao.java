package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Account;
import com.mpangoEngine.core.model.AccountGroup;
import com.mpangoEngine.core.model.Farm;

@Transactional
@Repository
public interface AccountDao {

	Account findAccountById(int id);
	
	List<Account> findAllAccountsByUser(int userId);
	
	List<Account> findAllAccounts();
	
	List<ChartOfAccounts> fetchUserCoa(int userId);
	
	Boolean existsById(int id);
	
	void save(Account account);
	
	List<AccountGroup> getAccountGroups();
}
