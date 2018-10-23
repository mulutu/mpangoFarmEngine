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

@Entity
@Table(name = "expense")
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name="user_id")
	private int UserId;

	@Column(name="expense_date")
	private Date ExpenseDate;

	@Column(name="supplier_id")
	private int SupplierId;

	private BigDecimal Amount;

	@Column(name="payment_method_id")
	private int PaymentMethodId;
	
	@Column(name="account_id")
	private int AccountId;

	@Column(name="project_id")
	private int ProjectId;

	private String Notes;

	// ----------------------

	
	@Transient
	private String Supplier;

	@Transient
	private String Account;

	@Transient
	private String ProjectName;

	@Transient
	private String PaymentMethod;

	@Transient
	private String FarmName;

	// ----------------------

	public Expense() {
		id = 0;
	}

	/*public Expense(Date ExpenseDate_, int userId, int supplierId, Integer Amount_, int paymentMethodId,
			String Account_, int projectId, String projectName, String Notes_) {
		ExpenseDate = ExpenseDate_;
		UserId = userId;
		SupplierId = supplierId;
		Amount = Amount_;
		PaymentMethodId = paymentMethodId;
		Account = Account_;
		ProjectId = projectId;
		Notes = Notes_;
	}*/
	public Expense(BigDecimal amount, int supplierId, int paymentMethodId, int accountId, int projectId, String notes, int userId) {
		ExpenseDate = new Date();
		Amount = amount;
		SupplierId = supplierId;
		PaymentMethodId = paymentMethodId;
		AccountId = accountId;
		ProjectId = projectId;
		Notes = notes;
		UserId = userId;
	}

	
	public String getSupplier() {
		return Supplier;
	}

	public void setSupplier(String supplier) {
		Supplier = supplier;
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

	public Date getExpenseDate() {
		return ExpenseDate;
	}

	public void setExpenseDate(Date expDate) {
		ExpenseDate = expDate;
	}

	public int getSupplierId() {
		return SupplierId;
	}

	public void setSupplierId(int supplierId) {
		SupplierId = supplierId;
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
		Expense other = (Expense) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", ExpenseDate=" + ExpenseDate + ", SupplierId=" + SupplierId + ", Amount=" + Amount
				+ ", PaymentMethodId=" + PaymentMethodId + ", AccountId=" + AccountId + ", SupplierId=" + SupplierId
				+ ", Notes=" + Notes + ", UserId=" + UserId + "]";
	}

}
