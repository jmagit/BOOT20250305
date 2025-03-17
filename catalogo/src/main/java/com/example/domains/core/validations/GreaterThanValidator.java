package com.example.domains.core.validations;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GreaterThanValidator implements ConstraintValidator<GreaterThan, Object> {
	private String minor;
	private String major;
	
	@Override
	public void initialize(GreaterThan constraintAnnotation) {
		minor = constraintAnnotation.minor();
		major = constraintAnnotation.major();
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null) return false;
		try {
			var minorFld = value.getClass().getDeclaredField(minor);
			minorFld.setAccessible(true);
			var minorValue = minorFld.get(value);
			var majorFld = value.getClass().getDeclaredField(major);
			majorFld.setAccessible(true);
			var majorValue = majorFld.get(value);
			if(minorValue == null || majorValue == null || minorValue == majorValue || minorValue.getClass() != majorValue.getClass()) 
				return false;
			if(minorValue instanceof Comparable c)
				return c.compareTo(majorValue) < 0;
			if(minorValue instanceof Number c)
				return c.doubleValue() < ((Number)majorValue).doubleValue();
			return minorValue.toString().compareTo(majorValue.toString()) < 0;
		} catch (Exception e) {
			return false;
		}
	}
}
