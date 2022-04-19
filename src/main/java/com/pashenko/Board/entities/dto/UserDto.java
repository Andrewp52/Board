package com.pashenko.Board.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "First name is mandatory")
    @Size(min=2, max=30)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min=2, max=30)
    private String lastName;

    @NotBlank(message = "Phone is mandatory")
    @Size(max = 20)
    private String phone;

    @NotBlank(message = "E-mail is mandatory")
    @Email
    @Size(min = 5, max = 30)
    private String email;


}
