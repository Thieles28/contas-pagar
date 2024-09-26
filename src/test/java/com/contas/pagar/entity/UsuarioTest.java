package com.contas.pagar.entity;

import com.contas.pagar.domain.Role;
import com.contas.pagar.model.factory.ContaFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(0L, "nome", "email", "senha", Role.USER);
    }

    @Test
    void testGetAuthorities() {
        // Setup
        // Run the test
        final Collection<? extends GrantedAuthority> result = usuario.getAuthorities();

        // Verify the results
    }

    @Test
    void testSenhaGetterAndSetter() {
        Usuario usuarios = ContaFactory.criarUsuario();
        assertThat(usuarios.getPassword()).isEqualTo("1234");
    }

    @Test
    void testEmailGetterAndSetter() {
        Usuario usuarios = ContaFactory.criarUsuario();
        assertThat(usuarios.getEmail()).isEqualTo("pedro@gmail.com");
    }

    @Test
    void testIsAccountNonExpired() {
        assertThat(usuario.isAccountNonExpired()).isTrue();
    }

    @Test
    void testIsAccountNonLocked() {
        assertThat(usuario.isAccountNonLocked()).isTrue();
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertThat(usuario.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testIsEnabled() {
        assertThat(usuario.isEnabled()).isTrue();
    }
}
