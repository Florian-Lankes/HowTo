package com.HowTo.spring_boot_HowTo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
@EntityListeners(AuditListener.class)
public class Wallet extends Auditable{

	@Id
	@Column(name = "user_id")
	private Long walletId;

	@MapsId
	@OneToOne
	@JoinColumn(name = "user_Id")
	@JsonIgnore
	private User walletOwner;
	
	private Long amount;
	
	private WalletPlan walletPlan;
	
	@JsonManagedReference(value = "wallet-sender")
	@OneToMany(mappedBy ="transactionSender", cascade = CascadeType.ALL)
	private List<Transaction> sendTransactions;
	
	@JsonManagedReference(value = "wallet-receiver")
	@OneToMany(mappedBy ="transactionReceiver", cascade = CascadeType.ALL)
	private List<Transaction> receivedTransactions;
	
	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public WalletPlan getWalletPlan() {
		return walletPlan;
	}

	public void setWalletPlan(WalletPlan walletPlan) {
		this.walletPlan = walletPlan;
	}

	public User getWalletOwner() {
		return walletOwner;
	}

	public void setWalletOwner(User walletOwner) {
		this.walletOwner = walletOwner;
	}
	

}

