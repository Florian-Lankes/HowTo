package com.HowTo.spring_boot_HowTo.model;

public enum WalletPlan {
	BASIC(1),
	BASICPLUS(0.9),
	PREMIUM(0.8), 
	PRO(0.7);
	
	private final double benefit;
	
	WalletPlan(double benefit){
		this.benefit = benefit;
	}
	public double getBenefit() {
		return benefit;
	}
}

