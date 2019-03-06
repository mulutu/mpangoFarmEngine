package com.mpangoEngine.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

public class AccountGroup {

	private int id;
	private String accountGroupTypeCode;
	private String accountGroupTypeName;
		

	public AccountGroup() {
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getAccountGroupTypeCode() {
		return accountGroupTypeCode;
	}


	public void setAccountGroupTypeCode(String accountGroupTypeCode) {
		this.accountGroupTypeCode = accountGroupTypeCode;
	}


	public String getAccountGroupTypeName() {
		return accountGroupTypeName;
	}


	public void setAccountGroupTypeName(String accountGroupTypeName) {
		this.accountGroupTypeName = accountGroupTypeName;
	}		
	
	

}
