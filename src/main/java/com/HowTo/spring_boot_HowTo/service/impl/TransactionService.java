package com.HowTo.spring_boot_HowTo.service.impl;


import com.HowTo.spring_boot_HowTo.model.Transaction;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.Wallet;
import com.HowTo.spring_boot_HowTo.repository.TransactionRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.WalletRepositoryI;
import com.HowTo.spring_boot_HowTo.service.TransactionServiceI;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TransactionService implements TransactionServiceI {
	
	@Autowired
	UserRepositoryI userRepository;
	@Autowired
	TransactionRepositoryI transactionRepository;
	@Autowired
	WalletRepositoryI walletRepository;

	
	@Override
	public List<Transaction> getAllTransactions() {
		// TODO Auto-generated method stub
		return transactionRepository.findAll();
	}

	@Override
	public Transaction saveTransaction(Transaction transaction, Long userid) {
		User user = userRepository.findById(userid).get();
		List<Transaction> transactionList = getAllTransactions();
        if(user != null && transaction != null) {
            if(!transactionList.contains(transaction)) {
            	transactionRepository.save(transaction);
            }
        }
		return transaction;
	}
	
	@Override
	public Transaction getTransactionById(Long id) {
		// TODO Auto-generated method stub
		Optional<Transaction> transaction = transactionRepository.findById(id);
		return transaction.isPresent()? transaction.get():null;
	}
	
	
	@Override
	public List<Transaction> getTransactionByTransactionSender(Long id) {
		// TODO Auto-generated method stub
		Wallet wallet = walletRepository.findById(id).get();
		List<Transaction> transaction = transactionRepository.findByTransactionSender(wallet);
		return transaction;
	}
	
	
	@Override
	public List<Transaction> getTransactionByTransactionReceiver(Long id) {
		// TODO Auto-generated method stub
		Wallet wallet = walletRepository.findById(id).get();
		List<Transaction> transaction = transactionRepository.findByTransactionReceiver(wallet);
		return transaction;
	}
	

}
