package com.godev.linkhubservice.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class AccountRequest {

    @Length(min = 4, max = 20, message = "The username field must have between 4 and 20 characters")
    @NotBlank(message = "The username field is required!")
    private String name;

    @Length(min = 6, max = 50, message = "The email field must have between 6 and 50 characters")
    @NotBlank(message = "The email field is required!")
    private String email;

    @Length(min = 8, max = 16, message = "The password field must have between 8 and 16 characters")
    @NotBlank(message = "The password field is required!")
    private String password;

}
