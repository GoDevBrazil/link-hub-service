package com.godev.linkhubservice.domain.vo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
//    @NotBlank(message = "The email field is required!")
    @Pattern(regexp = "/^[a-z0-9!#$%&'*+=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/",
            message = "que email é esse rapaz?")
    private String email;

//    @Length(min = 8, max = 16, message = "The password field must have between 8 and 16 characters")
//    @NotBlank(message = "The password field is required!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$",
            message = "password must be min 8 and max 16 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;

}
