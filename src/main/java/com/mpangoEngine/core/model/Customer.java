package com.mpangoEngine.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "date_created")
	private Date DateCreated;
	
	@Column(name = "user_id")
	private int userID;
	
	@Column(name = "customer_names")
	private String CustomerNames;
	
	private String Description;

	public Customer() {
		id = 0;
	}
	
	

	public Date getDateCreated() {
		return DateCreated;
	}



	public void setDateCreated(Date dateCreated) {
		DateCreated = dateCreated;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getCustomerNames() {
		return CustomerNames;
	}

	public void setCustomerNames(String customerNames) {
		CustomerNames = customerNames;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	
	

}
