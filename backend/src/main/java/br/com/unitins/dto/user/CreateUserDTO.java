package br.com.unitins.dto.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Pattern;

@Setter
@Getter
public class CreateUserDTO {

    private Long id;
    @Pattern(regexp = "^[a-z]+$\n", message = "Enter the nickname in lower case, without spaces and without special characters")
    private String nickName;
    private String name;
    private String surname;
    private String email;
}
