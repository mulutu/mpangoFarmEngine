package com.mpangoEngine.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "chart_of_accounts")
public class ChartOfAccounts {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name="account_name") 	private String AccountName;	
	@Column(name="account_code") 	private String AccountCode;	
	@Column(name="account_type_id") 	private long AccountTypeId;	
	@Column(name="description") 	private String description;	
	@Column(name="user_id") 	private long userId;
	
	//-----------------
	@Transient
	private String accountTypeCode;
	
	@Transient
	private String accountTypeName;	
	
	@Transient
	private String accountGroupTypeCode;
	
	@Transient
	private int accountGroupTypeId;
	
	@Transient
	private String accountGroupTypeName;	

	public ChartOfAccounts() {
		//id = 0;
	}		
	
	

	public int getAccountGroupTypeId() {
		return accountGroupTypeId;
	}



	public void setAccountGroupTypeId(int accountGroupTypeId) {
		this.accountGroupTypeId = accountGroupTypeId;
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



	public String getAccountTypeCode() {
		return accountTypeCode;
	}



	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}



	public String getAccountTypeName() {
		return accountTypeName;
	}



	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}



	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}



	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountName() {
		return AccountName;
	}

	public void setAccountName(String accountName) {
		AccountName = accountName;
	}

	public String getAccountCode() {
		return AccountCode;
	}

	public void setAccountCode(String accountCode) {
		AccountCode = accountCode;
	}

	public long getAccountTypeId() {
		return AccountTypeId;
	}

	public void setAccountTypeId(long accountTypeId) {
		AccountTypeId = accountTypeId;
	}

}
