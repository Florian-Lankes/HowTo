package com.HowTo.spring_boot_HowTo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.Report;
import com.HowTo.spring_boot_HowTo.model.Transaction;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.Wallet;
import com.HowTo.spring_boot_HowTo.model.WalletPlan;
import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
import com.HowTo.spring_boot_HowTo.service.TransactionServiceI;
import com.HowTo.spring_boot_HowTo.service.WalletServiceI;
import com.HowTo.spring_boot_HowTo.service.impl.UserService;

import jakarta.validation.Valid;

import com.HowTo.spring_boot_HowTo.service.UserServiceI;


@Controller
@RequestMapping("/wallet")
public class WalletController {

	
	private WalletServiceI walletService;
	private TransactionServiceI transactionService;
	private UserServiceI userService;
	
	public WalletController(WalletServiceI walletService, TransactionServiceI transactionService,  UserServiceI userService) {
		super();
		this.walletService = walletService;
		this.transactionService = transactionService;
		this.userService = userService;
	}
		
		
		
	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal() instanceof String) {
			throw new IllegalStateException("User is not authenticated");
		}
		User user = (User) authentication.getPrincipal();
		return user.getUserId();
	}

	@GetMapping("/create")
    public String addWallet(Model model) {
    	
		Long userId =  getCurrentUserId();

    	Wallet wallet = new Wallet();
    	walletService.saveWallet(wallet, userId);
    	
		model.addAttribute("wallet", walletService.getWalletById(userId));
    	
//    	model.addAttribute("transactionsReceived", transactionService.getTransactionByTransactionReceiver(userId));
//    	model.addAttribute("transactionsSend", transactionService.getTransactionByTransactionSender(userId));
//        
        return "users/wallet";
    }
	
	@GetMapping("/my")
    public String openWallet(Model model) {
    	
		Long userId =  getCurrentUserId();

    	model.addAttribute("wallet", walletService.getWalletById(userId));
    	
    	model.addAttribute("transactionsReceived", transactionService.getTransactionByTransactionReceiver(userId));
    	model.addAttribute("transactionsSend", transactionService.getTransactionByTransactionSender(userId));
    	model.addAttribute("user", userService.getAllUsers());
    	
        return "users/wallet";
    }
	@GetMapping("/donate/{id}")
    public String openDonate(@PathVariable("id") Long receiverUserId, Model model) {
    	
		Long senderUserId =  getCurrentUserId();
		
		Transaction transactionForm = new Transaction();

    	model.addAttribute("senderWallet", walletService.getWalletById(senderUserId));
    	model.addAttribute("receiver", userService.getUserById(receiverUserId));
    	transactionForm.setTransactionSender(walletService.getWalletById(senderUserId));
    	transactionForm.setTransactionReceiver(walletService.getWalletById(receiverUserId));
    	
    	
    	model.addAttribute("transaction", transactionForm);
 
        return "channels/donate";
    }
	
	@PostMapping("/donate")
    public String openDonate(@ModelAttribute Transaction transaction, Model model, BindingResult result, RedirectAttributes redirectAttributes) {
    	
		if (result.hasErrors()) {
    		System.out.println(result.getAllErrors().toString());
            return "channels/donate";
        }
    	
    	
    	transactionService.saveTransaction(transaction, getCurrentUserId());
    	
    	long amount = transaction.getTransactionAmount();
    	
    	Wallet receiverWallet = walletService.getWalletById(transaction.getTransactionReceiver().getWalletOwner().getUserId());
    	receiverWallet.setAmount(receiverWallet.getAmount()+amount);
    	
    	
    	Wallet senderWallet = walletService.getWalletById(getCurrentUserId());
    	amount = (long) (amount*senderWallet.getWalletPlan().getBenefit());
    	senderWallet.setAmount(senderWallet.getAmount()-amount);
    	
    	walletService.updateWallet(senderWallet);
    	walletService.updateWallet(receiverWallet);
    	
        redirectAttributes.addFlashAttribute("added", "Donation abgeschlossen");
        
        return "redirect:/wallet/my";
    }
	@GetMapping("/upgrade")
    public String upgrade(@Valid @ModelAttribute Wallet wallet, RedirectAttributes redirectAttributes){
		
		
		wallet = walletService.getWalletById(getCurrentUserId());
    	
		if(wallet.getWalletPlan() == WalletPlan.BASIC && wallet.getAmount()>=100) {
			wallet.setWalletPlan(WalletPlan.BASICPLUS);
			wallet.setAmount(wallet.getAmount()-100);
			walletService.updateWallet(wallet);
			redirectAttributes.addFlashAttribute("added", "Upgrade konnte vervollständigt werden");
			return "redirect:/wallet/my";
		}else if(wallet.getWalletPlan() == WalletPlan.BASICPLUS && wallet.getAmount()>=200){
			wallet.setWalletPlan(WalletPlan.PREMIUM);
			wallet.setAmount(wallet.getAmount()-200);
			walletService.updateWallet(wallet);
			redirectAttributes.addFlashAttribute("added", "Upgrade konnte vervollständigt werden");
			return "redirect:/wallet/my";
		}else if(wallet.getWalletPlan() == WalletPlan.PREMIUM && wallet.getAmount()>=300){
			wallet.setWalletPlan(WalletPlan.PRO);
			wallet.setAmount(wallet.getAmount()-300);
			walletService.updateWallet(wallet);
			redirectAttributes.addFlashAttribute("added", "Upgrade konnte vervollständigt werden");
			return "redirect:/wallet/my";
		}
		
		 redirectAttributes.addFlashAttribute("deleted", "Upgrade konnte nicht vervollständigt werden");
		
		

    	return "redirect:/wallet/my";
    }
}
