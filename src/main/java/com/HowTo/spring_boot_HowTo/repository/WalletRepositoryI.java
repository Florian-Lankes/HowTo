package com.HowTo.spring_boot_HowTo.repository;


import org.springframework.data.jpa.repository.JpaRepository;


import com.HowTo.spring_boot_HowTo.model.Wallet;

public interface WalletRepositoryI extends JpaRepository<Wallet, Long>{

}
