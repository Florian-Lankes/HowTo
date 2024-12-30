package com.HowTo.spring_boot_HowTo.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.HowTo.spring_boot_HowTo.model.User;

public class UserValidator implements Validator{

	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// TODO Auto-generated method stub
		User user = (User) target;
		
		long age = ChronoUnit.YEARS.between(user.getBirthDate(), LocalDate.now());
		System.out.println("Age in validator: " + age);
		
		if (age<16) {
			errors.rejectValue("birthDate", "user.birthDate.less16");
			
		}

	}
}
