package com.mpangoEngine.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "coa_account_type")
public class COAAccountType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 	private int id;

	@Column(name="account_type_name") 	private String AccountTypeName;	
	@Column(name="account_type_code") 	private String AccountTypeCode;	
	@Column(name="account_group_type_id") 	private int AccountGroupTypeId;	

	public COAAccountType() {
		id = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountTypeName() {
		return AccountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		AccountTypeName = accountTypeName;
	}

	public String getAccountTypeCode() {
		return AccountTypeCode;
	}

	public void setAccountTypeCode(String accountTypeCode) {
		AccountTypeCode = accountTypeCode;
	}

	public int getAccountGroupTypeId() {
		return AccountGroupTypeId;
	}

	public void setAccountGroupTypeId(int accountGroupTypeId) {
		AccountGroupTypeId = accountGroupTypeId;
	}		
	
	

}
