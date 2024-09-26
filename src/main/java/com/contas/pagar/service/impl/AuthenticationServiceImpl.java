package com.contas.pagar.service.impl;

import com.contas.pagar.domain.Role;
import com.contas.pagar.entity.Usuario;
import com.contas.pagar.model.request.LoginRequest;
import com.contas.pagar.model.request.RegistrarUsuarioRequest;
import com.contas.pagar.model.response.JwtAuthenticationResponse;
import com.contas.pagar.repository.UsuarioRepository;
import com.contas.pagar.service.AuthenticationService;
import com.contas.pagar.service.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(RegistrarUsuarioRequest request) {
        var user = Usuario.builder().nome(request.getNome())
                .email(request.getEmail()).senha(passwordEncoder.encode(request.getSenha()))
                .role(Role.USER).build();
        usuarioRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
        var user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @PostConstruct
    public void initializeUserData() {
        if (usuarioRepository.findByEmail("thieles@gmail.com").isEmpty()) {
            Usuario usuario = Usuario.builder()
                    .nome("Thieles")
                    .email("thieles@gmail.com")
                    .senha(passwordEncoder.encode("90tmr2810"))
                    .role(Role.ADMIN)
                    .build();
            usuarioRepository.save(usuario);
        }
    }
}
