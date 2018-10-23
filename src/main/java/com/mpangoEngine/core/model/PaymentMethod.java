package com.mpangoEngine.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="payment_method")
	private String PaymentMethod;
	
	@Column(name="date_created")
	private Date DateCreated;

	public PaymentMethod() {
		id = 0;
	}

	public PaymentMethod(String paymentMethod) {
		PaymentMethod = paymentMethod;
		DateCreated = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPaymentMethod() {
		return PaymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		PaymentMethod = paymentMethod;
	}

	public Date getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		DateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", PaymentMethod=" + PaymentMethod + ", DateCreated=" + DateCreated + "]";
	}
}
