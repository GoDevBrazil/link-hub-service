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

    @Length(min = 4, max = 20, message = "O campo nome precisa ter entre 4 e 20 caracteres.")
    @NotBlank(message = "O campo nome é obrigatório!")
    private String name;

    @Length(min = 6, max = 50, message = "O campo email precisa ter entre 6 e 50 caracteres.")
    @NotBlank(message = "O campo email é obrigatório!")
    @Pattern(regexp = "^[a-z0-9!#$%&'*+=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$",
            message = "O campo email é precisa ser preenchido com um e-mail no formato válido.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$",
            message = "O campo senha precisa ter entre 8 e 16 caracteres contendo pelo menos uma letra maíúscula, uma minúscula, um número e um caractere especial.")
    private String password;

}
