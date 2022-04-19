package com.pashenko.Board.entities.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserRegDto extends UserDto{
    @NotBlank
    @Size(min = 3, max = 30)
    private String password;
    private List<RoleDto> roles;

    public UserRegDto(Long id, String firstName, String lastName, String phone, String email, String password, List<RoleDto> roles) {
        super(id, firstName, lastName, phone, email);
        this.password = password;
        this.roles = roles;
    }

    public UserRegDto() {
    }
}
