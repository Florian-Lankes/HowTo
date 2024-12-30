package com.HowTo.spring_boot_HowTo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Channel {

	@NotNull
	private User user;
	
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String channelname;
	
	@Size(min = 0, max = 500, message = "{jakarta.validation.constraints.Size}")
	private String description;
	
	
}
