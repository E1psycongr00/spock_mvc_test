package com.spock.test_demo.validation.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private final static String PHONE_PATTERN = "\\d{3}-\\d{4}-\\d{4}";
    @Override
    public void initialize(Phone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Matcher matcher = Pattern.compile(PHONE_PATTERN).matcher(value);
        return matcher.matches();
    }
}
