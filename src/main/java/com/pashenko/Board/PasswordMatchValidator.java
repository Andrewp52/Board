package com.pashenko.Board;

import com.pashenko.Board.entities.dto.PassChangeDto;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, PassChangeDto> {
    @Override
    public boolean isValid(PassChangeDto value, ConstraintValidatorContext context) {
        return value.getNewPass().equals(value.getNewPassRepeat());
    }
}
