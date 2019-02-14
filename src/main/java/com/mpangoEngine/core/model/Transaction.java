package com.mpangoEngine.core.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mpangoEngine.core.util.JsonDateDeserializer;
import com.mpangoEngine.core.util.JsonDateSerializer;

public class Transaction {
	
	private int transactionID;
    private BigDecimal transactionAmount;
    private Date transactionDate;
    private String transactionDescription;
    private String transactionType;
    
    public Transaction() {}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getTransactionDate() {
		return transactionDate;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class, as = Date.class)
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
    
    

}
