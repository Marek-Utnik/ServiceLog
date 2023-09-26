package com.servicedata.servicelogs.validators;

import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.SystemUserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameUniquenessValidator
        implements ConstraintValidator<UniqueUsername, SystemUser> {

    private final SystemUserRepository systemUserRepository;
    
    public UsernameUniquenessValidator(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SystemUser systemUser, ConstraintValidatorContext ctx) {
    	SystemUser foundSystemUser = systemUserRepository.findByUsername(systemUser.getUsername());

        if (foundSystemUser == null) {
            return true;
        }

        boolean usernameIsUnique = foundSystemUser.getSystemUserId() != null && foundSystemUser.getSystemUserId().equals(systemUser.getSystemUserId());

        if (!usernameIsUnique) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        return usernameIsUnique;
    }
}