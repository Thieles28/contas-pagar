package com.contas.pagar.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email não pode estar em branco")
    private String email;
    @NotBlank(message = "Senha não pode estar em branco")
    private String senha;
}
