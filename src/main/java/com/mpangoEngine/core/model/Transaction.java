package com.mpangoEngine.core.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mpangoEngine.core.util.JsonDateDeserializer;
import com.mpangoEngine.core.util.JsonDateSerializer;
import java.util.ArrayList;
import java.util.List;

@Component
public class Transaction {

    private int id;
    private int accountId;
    private BigDecimal amount;
    private Date transactionDate;
    private int transactionTypeId;
    private String transactionType;
    private String description;
    private int projectId;
    private String projectName;
    private int userId;
    private int farmId;
    private String farmName;
    private String accountName;

    private List<Integer> selectedProjectIds = new ArrayList<Integer>();

    public Transaction() {
    }

    public Transaction(Date transactionDate, BigDecimal amount, int accountId, int projectId, String description, int userId) {
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.accountId = accountId;
        this.projectId = projectId;
        this.description = description;
        this.userId = userId;
    }

    public Transaction(Date transactionDate, BigDecimal amount, int accountId, int projectId, String description, int userId, int transactionTypeId) {
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.accountId = accountId;
        this.projectId = projectId;
        this.description = description;
        this.userId = userId;
        this.transactionTypeId = transactionTypeId;
    }

    public Transaction(int id, Date transactionDate, BigDecimal amount, int accountId, int projectId, String description) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.accountId = accountId;
        this.projectId = projectId;
        this.description = description;
    }

    /**
     * @return the selectedProjectIds
     */
    public List<Integer> getSelectedProjectIds() {
        return selectedProjectIds;
    }

    /**
     * @param selectedProjectIds the selectedProjectIds to set
     */
    public void setSelectedProjectIds(List<Integer> selectedProjectIds) {
        this.selectedProjectIds = selectedProjectIds;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
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
    public String toString() {
        return "Transaction ["
                + "id=" + id + ", "
                + "accountId=" + accountId + ", "
                + "amount=" + amount + ", "
                + "transactionDate=" + transactionDate + ", "
                + "transactionTypeId=" + transactionTypeId + ", "
                + "description=" + description + ", "
                + "projectId=" + projectId + ", "
                + "userId=" + userId
                + "]";
    }

}
