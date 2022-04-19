package com.pashenko.Board.entities.dto;

import com.pashenko.Board.PasswordMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatch
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
}
