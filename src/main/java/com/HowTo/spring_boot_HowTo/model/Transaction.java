package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Transaction  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	@ManyToOne
	private Wallet transactionSender;
	
	@ManyToOne
	private Wallet transactionReceiver;
	
	private Long transactionAmount;

	public Long getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Wallet getTransactionSender() {
		return transactionSender;
	}

	public void setTransactionSender(Wallet transactionSender) {
		this.transactionSender = transactionSender;
	}

	public Wallet getTransactionReceiver() {
		return transactionReceiver;
	}

	public void setTransactionReceiver(Wallet transactionReceiver) {
		this.transactionReceiver = transactionReceiver;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	
	
}


