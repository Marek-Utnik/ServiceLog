package com.servicedata.servicelogs.validators;

import com.servicedata.servicelogs.models.SystemUser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPasswordsValidator implements ConstraintValidator<ValidPasswords, SystemUser> {

    @Override
    public void initialize(ValidPasswords constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SystemUser systemUser, ConstraintValidatorContext ctx) {
        if (systemUser.getPassword() == null || systemUser.getPassword().equals("")) {
            if (systemUser.getSystemUserId() == null) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("password")
                        .addConstraintViolation();
                return false;
            } else {
                return true;
            }
        }

        boolean passwordsAreValid = systemUser.getPassword().equals(systemUser.getRepeatedPassword());

        if (passwordsAreValid) {
            return true;
        } else {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("repeatedPassword")
                    .addConstraintViolation();
            return false;
        }
    }
}