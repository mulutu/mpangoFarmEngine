package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.COAAccountType;
import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Farm;

@Transactional
@Repository
public interface ChartOfAccountsDao {

	ChartOfAccounts findAccountById(int id);
	
	List<ChartOfAccounts> findAllCOAByUser(int userId);
	
	List<ChartOfAccounts> findAllCOA();
	
	List<COAAccountType> fetchAllAccountTypes();
	
	Boolean existsById(int id);
	
	void save(ChartOfAccounts chartOfAccount);
}
