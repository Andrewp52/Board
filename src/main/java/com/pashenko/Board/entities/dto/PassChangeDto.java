package com.pashenko.Board.entities.dto;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PassChangeDto {
    private Long userId;
    private String oldPass;

    @NotBlank
    @Size(min = 3, max = 30)
    private String newPass;

    @NotBlank
    @Size(min = 3, max = 30)
    private String newPassRepeat;

    public PassChangeDto(Long userId) {
        this.userId = userId;
    }

    @AssertTrue
    public boolean isPasswordsMatch(){
        return newPass.equals(newPassRepeat);
    }
}
