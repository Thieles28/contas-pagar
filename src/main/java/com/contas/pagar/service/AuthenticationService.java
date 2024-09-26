package com.contas.pagar.service;

import com.contas.pagar.model.request.LoginRequest;
import com.contas.pagar.model.request.RegistrarUsuarioRequest;
import com.contas.pagar.model.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(RegistrarUsuarioRequest request);
    JwtAuthenticationResponse signin(LoginRequest request);
}
