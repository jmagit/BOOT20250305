package com.example.domains.core.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GreaterThan.Validator.class)
@Documented
public @interface GreaterThan {
	String message() default "{validation.GreaterThan.message}";
	String minor();
	String major();
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<GreaterThan, Object> {
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
}
