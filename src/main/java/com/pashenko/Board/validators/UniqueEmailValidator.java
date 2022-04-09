package com.pashenko.Board.validators;

import com.pashenko.Board.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserService userService;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            userService.loadUserByUsername(value);
            return false;
        } catch (UsernameNotFoundException e){
            return true;
        }
    }


}
