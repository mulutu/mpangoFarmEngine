package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Transaction;

@Transactional
@Repository
public interface TransactionDao {

	Boolean transactionExistsById(int id);

	Transaction findTransactionById(int id);

	int saveTransaction(Transaction transaction);

	int updateTransaction(Transaction transaction);

	List<Transaction> findAll();

	public List<Transaction> findUserTransactions(int userId);

}
