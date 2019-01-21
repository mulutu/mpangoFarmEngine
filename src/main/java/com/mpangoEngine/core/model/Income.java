package com.mpangoEngine.core.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mpangoEngine.core.util.JsonDateDeserializer;
import com.mpangoEngine.core.util.JsonDateSerializer;

@Entity
@Table(name = "income")
public class Income {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "user_id")
	private int UserId;

	@Column(name = "income_date")
	private Date IncomeDate;

	@Column(name = "customer_id")
	private int CustomerId;

	@Column(name = "amount")
	private BigDecimal Amount;

	@Column(name = "payment_method_id")
	private int PaymentMethodId;

	@Column(name = "account_id")
	private int AccountId;

	@Column(name = "project_id")
	private int ProjectId;

	private String Notes;

	// ----------------------

	@Transient
	private String Customer;

	@Transient
	private String Account;

	@Transient
	private String ProjectName;

	@Transient
	private String PaymentMethod;

	@Transient
	private String FarmName;

	// ----------------------

	public Income() {
		//id = 0;
	}

	public Income(BigDecimal amount, int customerId, int paymentMethodId, int accountId, int projectId, String notes, int userId) {
		IncomeDate = new Date();
		Amount = amount;
		CustomerId = customerId;
		PaymentMethodId = paymentMethodId;
		AccountId = accountId;
		ProjectId = projectId;
		Notes = notes;
		UserId = userId;
	}
	
	public Income(Date incomeDate, BigDecimal amount, int customerId, int paymentMethodId, int accountId, int projectId, String notes, int userId) {
		IncomeDate = incomeDate;
		Amount = amount;
		CustomerId = customerId;
		PaymentMethodId = paymentMethodId;
		AccountId = accountId;
		ProjectId = projectId;
		Notes = notes;
		UserId = userId;
	}


	public String getCustomer() {
		return Customer;
	}

	public void setCustomer(String customer) {
		Customer = customer;
	}

	public int getAccountId() {
		return AccountId;
	}

	public void setAccountId(int accountId) {
		AccountId = accountId;
	}

	public int getPaymentMethodId() {
		return PaymentMethodId;
	}

	public void setPaymentMethodId(int paymentMethodId) {
		PaymentMethodId = paymentMethodId;
	}

	public String getFarmName() {
		return FarmName;
	}

	public void setFarmName(String farmName) {
		FarmName = farmName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id_) {
		id = id_;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getIncomeDate() {
		return IncomeDate;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class, as = Date.class)
	public void setIncomeDate(Date incomeDate) {
		IncomeDate = incomeDate;
	}

	public int getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(int customerId) {
		CustomerId = customerId;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amt) {
		Amount = amt;
	}

	public String getPaymentMethod() {
		return PaymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		PaymentMethod = paymentMethod;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String acct) {
		Account = acct;
	}

	public int getProjectId() {
		return ProjectId;
	}

	public void setProjectId(int entr) {
		ProjectId = entr;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
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
		Income other = (Income) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", IncomeDate=" + IncomeDate + ", Customer=" + Customer + ", Amount=" + Amount
				+ ", PaymentMethodId=" + PaymentMethodId + ", AccountId=" + AccountId + ", CustomerId=" + CustomerId
				+ ", Notes=" + Notes + ", UserId=" + UserId + "]";
	}

}
