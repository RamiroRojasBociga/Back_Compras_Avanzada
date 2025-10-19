package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;

// Imports de seguridad y configuración de test
import com.sistemacompras.sistemacompras_api.config.JwtAuthenticationFilter;
import com.sistemacompras.sistemacompras_api.config.JwtTokenProvider;
import com.sistemacompras.sistemacompras_api.service.service.impl.CustomUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private UsuarioResponseDto usuarioResponseDto;

    @BeforeEach
    void setUp() {
        usuarioResponseDto = new UsuarioResponseDto();
        usuarioResponseDto.setIdUsuario(1L);
        usuarioResponseDto.setNombre("Juan Pérez");
        usuarioResponseDto.setEmail("juan.perez@example.com");
        usuarioResponseDto.setTelefono("0981234567");
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevoUsuarioYRetornar201() throws Exception {
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setNombre("Ana Gómez");
        requestDto.setEmail("ana.gomez@example.com");
        requestDto.setTelefono("0971987654");
        requestDto.setPassword("unaClaveSegura123"); // Campo obligatorio

        when(usuarioService.create(any(UsuarioRequestDto.class))).thenReturn(usuarioResponseDto);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarUsuarioExistente() throws Exception {
        // --- SOLUCIÓN: Creamos un DTO de solicitud completo y válido ---
        UsuarioRequestDto requestDto = new UsuarioRequestDto();
        requestDto.setNombre("Juan Pérez Actualizado");
        requestDto.setEmail("juan.actualizado@example.com"); // Campo obligatorio
        requestDto.setTelefono("0999888777");             // Campo obligatorio
        requestDto.setPassword("otraClaveSegura456");      // Campo obligatorio

        UsuarioResponseDto responseActualizado = new UsuarioResponseDto();
        responseActualizado.setIdUsuario(1L);
        responseActualizado.setNombre("Juan Pérez Actualizado");

        when(usuarioService.update(eq(1L), any(UsuarioRequestDto.class))).thenReturn(responseActualizado);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Pérez Actualizado")));
    }

    // Los tests para list, get y delete no necesitan cambios
    @Test
    @WithMockUser
    void list_DebeRetornarListaDeUsuarios() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuarioResponseDto));
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarUsuarioYRetornar204() throws Exception {
        doNothing().when(usuarioService).delete(1L);
        mockMvc.perform(delete("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
