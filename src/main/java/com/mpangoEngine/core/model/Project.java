package com.mpangoEngine.core.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
@Table(name = "project")
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) private int id;	
	@Column(name="user_id") private int UserId;	
	@Column(name="farm_id") private int FarmId;	
	@Column(name="project_name") private String ProjectName;	
	@Column(name="date_created") private Date DateCreated;	
	@Column(name="expected_output") private int expectedOutput;	
	@Column(name="actual_output") private int actualOutput;	
	@Column(name="unit_id") private int unitId;	
	@Transient private String unitDescription;
	
	@Transient private List<Transaction>  transactions;
	
	@Transient private BigDecimal totalExpeses;
	@Transient private BigDecimal totalIncomes;
	
	private String Description;

	public Project() {
		//id = 0;
	}

	public Project(int userId, int farmId, String projectName, String description) {
		UserId = userId;
		FarmId = farmId;
		ProjectName = projectName;
		Description = description;
		DateCreated = new Date();
	}
	
	
	
	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public BigDecimal getTotalExpeses() {
		return totalExpeses;
	}

	public void setTotalExpeses(BigDecimal totalExpeses) {
		this.totalExpeses = totalExpeses;
	}

	public BigDecimal getTotalIncomes() {
		return totalIncomes;
	}

	public void setTotalIncomes(BigDecimal totalIncomes) {
		this.totalIncomes = totalIncomes;
	}

	public String getUnitDescription() {
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}

	public int getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(int expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public int getActualOutput() {
		return actualOutput;
	}

	public void setActualOutput(int actualOutput) {
		this.actualOutput = actualOutput;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFarmId() {
		return FarmId;
	}

	public void setFarmId(int farmId) {
		FarmId = farmId;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDateCreated() {
		return DateCreated;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class, as = Date.class)
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
		return "Expense [id=" + id + ", UserId=" + UserId + ", ProjectName=" + ProjectName + ", DateCreated="
				+ DateCreated + ", Description=" + Description + "]";
	}

}
