package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Transaction;



public interface TransactionServiceI {

	List<Transaction> getAllTransactions();

	Transaction saveTransaction(Transaction transaction, Long userid);

	Transaction getTransactionById(Long id);

	List<Transaction> getTransactionByTransactionSender(Long id);

	List<Transaction> getTransactionByTransactionReceiver(Long id);



}
