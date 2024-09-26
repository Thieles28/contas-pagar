package com.contas.pagar.controller;

import com.contas.pagar.model.request.LoginRequest;
import com.contas.pagar.model.request.RegistrarUsuarioRequest;
import com.contas.pagar.model.response.JwtAuthenticationResponse;
import com.contas.pagar.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("registrar")
    public ResponseEntity<JwtAuthenticationResponse> signup(@Valid @RequestBody RegistrarUsuarioRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("login")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}
