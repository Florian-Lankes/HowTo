package com.HowTo.spring_boot_HowTo.validator;



import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.HowTo.spring_boot_HowTo.model.Tutorial;


public class TutorialValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Tutorial.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// TODO Auto-generated method stub
		Tutorial tutorial = (Tutorial) target;
		
	}

	
}
