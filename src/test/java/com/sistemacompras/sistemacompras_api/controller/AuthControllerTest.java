package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.config.JwtAuthenticationFilter;
import com.sistemacompras.sistemacompras_api.config.JwtTokenProvider;
import com.sistemacompras.sistemacompras_api.dto.LoginRequestDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
// ▼▼▼ ESTA ES LA RUTA CORRECTA QUE ME INDICASTE ▼▼▼
import com.sistemacompras.sistemacompras_api.service.service.impl.CustomUserDetailsServiceImpl;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Import estático para la solución del error 403 Forbidden (CSRF)
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Usamos @MockitoBean para todas las dependencias que el contexto de Spring necesita.
    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    // Aquí usamos la ruta correcta que me proporcionaste.
    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Test
    void login_ConCredencialesValidas_DebeRetornarOkYToken() throws Exception {
        // Given
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password123");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setEmail("test@email.com");
        usuarioEncontrado.setPassword("contraseña-hasheada-en-bd");

        when(usuarioService.findEntityByEmail("test@email.com")).thenReturn(usuarioEncontrado);
        when(passwordEncoder.matches("password123", "contraseña-hasheada-en-bd")).thenReturn(true);
        when(jwtTokenProvider.generarToken("test@email.com")).thenReturn("un-token-jwt-valido-y-generado");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        // Solución para el error 403 Forbidden
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Login correcto"))
                .andExpect(jsonPath("$.token").value("un-token-jwt-valido-y-generado"));
    }

    @Test
    void login_ConPasswordIncorrecta_DebeRetornarUnauthorized() throws Exception {
        // Given
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password-incorrecta");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setEmail("test@email.com");
        usuarioEncontrado.setPassword("contraseña-hasheada-en-bd");

        when(usuarioService.findEntityByEmail("test@email.com")).thenReturn(usuarioEncontrado);
        when(passwordEncoder.matches("password-incorrecta", "contraseña-hasheada-en-bd")).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        // Solución para el error 403 Forbidden
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Credenciales inválidas"));
    }
}
