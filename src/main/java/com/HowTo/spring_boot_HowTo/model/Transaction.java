package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	@JsonBackReference(value = "wallet-sender")
	@ManyToOne
	private Wallet transactionSender;
	
	@JsonBackReference(value = "wallet-receiver")
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


