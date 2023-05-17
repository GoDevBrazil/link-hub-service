package com.godev.linkhubservice.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "name", defaultValue = "John Doe", description = "Nome of a person or business")
    @Length(min = 4, max = 20, message = "O campo nome precisa ter entre 4 e 20 caracteres.")
    @NotBlank(message = "O campo nome é obrigatório!")
    private String name;

    @Schema(name = "email", defaultValue = "john.doe@email.com", description = "E-mail of a person or business")
    @Length(min = 6, max = 50, message = "O campo email precisa ter entre 6 e 50 caracteres.")
    @Pattern(regexp = "^[a-z0-9!#$%&'*+=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$",
            message = "O campo email é precisa ser preenchido com um e-mail no formato válido")
    private String email;

    @Schema(name = "password", defaultValue = "Makako@123", description = "Password of a person or business")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$",
            message = "O campo senha precisa ter entre 8 e 16 caracteres contendo pelo menos uma letra maíúscula, uma minúscula, um número e um caractere especial.")
    private String password;

}
