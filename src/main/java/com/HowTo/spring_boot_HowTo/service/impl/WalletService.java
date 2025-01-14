package com.HowTo.spring_boot_HowTo.service.impl;

import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.History;
import com.HowTo.spring_boot_HowTo.model.Rating;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.Wallet;
import com.HowTo.spring_boot_HowTo.model.WalletPlan;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.WalletRepositoryI;
import com.HowTo.spring_boot_HowTo.service.WalletServiceI;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WalletService implements WalletServiceI {
	
	@Autowired
	UserRepositoryI userRepository;
	@Autowired
	WalletRepositoryI walletRepository;

	
	@Override
	public List<Wallet> getAllWallets() {
		// TODO Auto-generated method stub
		return walletRepository.findAll();
	}

	@Override
	public Wallet saveWallet(Wallet wallet, Long userid) {
		User user = userRepository.findById(userid).get();
		List<Wallet> walletList = getAllWallets();
        if(user != null && wallet != null) {
            if(!walletList.contains(wallet)) {
//            	wallet.setWalletId(userid);
            	wallet.setAmount(500L);
            	wallet.setWalletPlan(WalletPlan.BASIC);   
            	wallet.setWalletOwner(user);
            	walletRepository.save(wallet);
            	user.setWallet(wallet);     	
            	
             	
            	userRepository.save(user);
            	System.out.println(wallet.getWalletPlan().getBenefit());
            	
            }
        }
		return wallet;
	}
	
	@Override
	public Wallet getWalletById(Long id) {
		// TODO Auto-generated method stub
		Optional<Wallet> wallet = walletRepository.findById(id);
		return wallet.isPresent()? wallet.get():null;
	}
	
	@Override
	public Wallet updateWallet(Wallet wallet) {
		Wallet local = walletRepository.save(wallet);
		return local;
	}

}
