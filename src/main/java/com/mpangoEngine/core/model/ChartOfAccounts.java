package com.mpangoEngine.core.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

public class ChartOfAccounts {
	private String AccountGroupTypeName;	
	private String AccountGroupTypeCode;	
	private int AccountGroupTypeId;
	private List<Account> listOfAccounts;

	public ChartOfAccounts() {
	}	
	
	

	public void setAccountGroupTypeCode(String accountGroupTypeCode) {
		AccountGroupTypeCode = accountGroupTypeCode;
	}



	public List<Account> getListOfAccounts() {
		return listOfAccounts;
	}

	public void setListOfAccounts(List<Account> listOfAccounts) {
		this.listOfAccounts = listOfAccounts;
	}

	public String getAccountGroupTypeName() {
		return AccountGroupTypeName;
	}

	public void setAccountGroupTypeName(String accountTypeName) {
		AccountGroupTypeName = accountTypeName;
	}

	public String getAccountGroupTypeCode() {
		return AccountGroupTypeCode;
	}

	public void getAccountGroupTypeCode(String accountTypeCode) {
		AccountGroupTypeCode = accountTypeCode;
	}

	public int getAccountGroupTypeId() {
		return AccountGroupTypeId;
	}

	public void setAccountGroupTypeId(int accountGroupTypeId) {
		AccountGroupTypeId = accountGroupTypeId;
	}		
	
	

}
