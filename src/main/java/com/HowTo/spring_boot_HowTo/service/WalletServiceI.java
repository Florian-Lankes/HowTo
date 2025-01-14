package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Rating;
import com.HowTo.spring_boot_HowTo.model.Wallet;

public interface WalletServiceI {
	
	List<Wallet> getAllWallets();
	
	Wallet saveWallet(Wallet wallet, Long userId);

	Wallet getWalletById(Long id);

	Wallet updateWallet(Wallet wallet);

	


}
