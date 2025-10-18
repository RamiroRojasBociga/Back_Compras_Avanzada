package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.config.JwtTokenProvider;
import com.sistemacompras.sistemacompras_api.dto.LoginRequestDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = AuthController.class,
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
        }
)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void testLogin_Success() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");

        when(usuarioService.findEntityByEmail("test@example.com")).thenReturn(usuario);
        when(jwtTokenProvider.generarToken("test@example.com")).thenReturn("jwt-token-here");

        // When & Then - CORREGIDO: usa "token" en lugar de "data"
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Login correcto"))
                .andExpect(jsonPath("$.token").value("jwt-token-here")); // Cambiado a "token"
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Given
        when(usuarioService.findEntityByEmail("test@example.com")).thenReturn(null);


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Credenciales inválidas"))
                .andExpect(jsonPath("$.token").isEmpty()); // Cambiado a "token"
    }

    @Test
    void testLogin_WrongPassword() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("correct-password");

        when(usuarioService.findEntityByEmail("test@example.com")).thenReturn(usuario);


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"wrong-password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Credenciales inválidas"))
                .andExpect(jsonPath("$.token").isEmpty()); // Cambiado a "token"
    }
}