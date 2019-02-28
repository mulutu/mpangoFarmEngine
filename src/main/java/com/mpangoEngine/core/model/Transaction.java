package com.mpangoEngine.core.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mpangoEngine.core.util.JsonDateDeserializer;
import com.mpangoEngine.core.util.JsonDateSerializer;

@Component
public class Transaction {

	private int id;
	private int accountId;
	private BigDecimal amount;
	private int customerSupplierId;
	private Date transactionDate;
	private int transactionTypeId;
	private String transactionType;
	private String description;
	private int paymentMethodId;
	private String paymentMethod;
	private int projectId;
	private String projectName;
	private int userId;	
	private String farmName;
	private String accountName;
	

	public Transaction() {
	}	
	

	public String getFarmName() {
		return farmName;
	}



	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}



	public String getAccountName() {
		return accountName;
	}



	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}



	public String getTransactionType() {
		return transactionType;
	}



	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}



	public String getPaymentMethod() {
		return paymentMethod;
	}



	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}



	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getCustomerSupplierId() {
		return customerSupplierId;
	}

	public void setCustomerSupplierId(int customerSupplierId) {
		this.customerSupplierId = customerSupplierId;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getTransactionDate() {
		return transactionDate;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class, as = Date.class)
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public int getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(int paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [" 
								+ "id=" + id + ", " 
								+ "accountId=" + accountId + ", " 
								+ "amount=" + amount + ", "
								+ "customerSupplierId=" + customerSupplierId + ", "
								+ "transactionDate=" + transactionDate + ", " 
								+ "transactionTypeId=" + transactionTypeId + ", "
								+ "description=" + description + ", "
								+ "paymentMethodId=" + paymentMethodId + ", " 
								+ "projectId=" + projectId + ", " 
								+ "userId=" + userId 
							+ "]";
	}
}
