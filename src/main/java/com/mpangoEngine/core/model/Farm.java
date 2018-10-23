package com.mpangoEngine.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "farm")
public class Farm {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	private int id;	
	@Column(name="user_id") private int UserId;	
	@Column(name="farm_name") private String FarmName;	
	private int Size; 
	private String Location;	
	@Column(name="date_created") private Date DateCreated;	
	private String Description;

	public Farm() {
		id = 0;
	}

	public Farm(int userId, String farmName, int size, String location, String description) {
		UserId = userId;
		FarmName = farmName;
		Size = size;
		Location = location;
		DateCreated = new Date();
		Description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getFarmName() {
		return FarmName;
	}

	public void setFarmName(String farmName) {
		FarmName = farmName;
	}

	public int getSize() {
		return Size;
	}

	public void setSize(int size) {
		Size = size;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public Date getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		DateCreated = dateCreated;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	@Override
	public String toString() {
		return "Expense ["
							+ "id=" + id + ", "
							+ "UserId=" + UserId + ", "
							+ "FarmName=" + FarmName + ", "
							+ "Size=" + Size + ", "
							+ "Location=" + Location 
					+ "]";
	}

}
