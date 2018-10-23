package com.mpangoEngine.core.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportObject {

	private long id;
	private BigDecimal TotalAmount;
	private String Description;


	public ReportObject() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		TotalAmount = totalAmount;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}
