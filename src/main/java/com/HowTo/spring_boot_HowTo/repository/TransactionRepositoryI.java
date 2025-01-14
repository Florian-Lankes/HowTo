package com.HowTo.spring_boot_HowTo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.Transaction;
import com.HowTo.spring_boot_HowTo.model.Wallet;


public interface TransactionRepositoryI extends JpaRepository<Transaction, Long>{

	List<Transaction> findByTransactionSender(Wallet wallet);

	List<Transaction> findByTransactionReceiver(Wallet wallet);
}
