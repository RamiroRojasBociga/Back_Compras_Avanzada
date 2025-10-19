package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.CiudadRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CiudadResponseDto;
import com.sistemacompras.sistemacompras_api.service.CiudadService;

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

// 1. @WebMvcTest: Enfocado solo en CiudadController.
@WebMvcTest(controllers = CiudadController.class,
        // Excluimos el filtro JWT para simplificar el test.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class CiudadControllerTest {

    // 2. Herramientas inyectadas por Spring.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 3. Mocks para las dependencias del contexto.
    @MockitoBean
    private CiudadService ciudadService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    // Objeto de prueba para reutilizar.
    private CiudadResponseDto ciudadResponseDto;

    @BeforeEach
    void setUp() {
        ciudadResponseDto = new CiudadResponseDto();
        ciudadResponseDto.setId(1L);
        ciudadResponseDto.setNombre("Asunción");
    }

    // 4. @WithMockUser: Simula un usuario autenticado para pasar la seguridad.
    @Test
    @WithMockUser
    void list_DebeRetornarListaDeCiudades() throws Exception {
        // Given
        when(ciudadService.findAll()).thenReturn(List.of(ciudadResponseDto));

        // When & Then
        mockMvc.perform(get("/api/ciudades"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Asunción")));
    }

    @Test
    @WithMockUser
    void get_DebeRetornarCiudadPorId() throws Exception {
        // Given
        when(ciudadService.findById(1L)).thenReturn(ciudadResponseDto);

        // When & Then
        mockMvc.perform(get("/api/ciudades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Asunción")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevaCiudadYRetornar201() throws Exception {
        // Given
        CiudadRequestDto requestDto = new CiudadRequestDto();
        requestDto.setNombre("Luque");

        when(ciudadService.create(any(CiudadRequestDto.class))).thenReturn(ciudadResponseDto);

        // When & Then
        mockMvc.perform(post("/api/ciudades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        // 5. .with(csrf()): Necesario para peticiones que modifican datos.
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Asunción")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarCiudadExistente() throws Exception {
        // Given
        CiudadRequestDto requestDto = new CiudadRequestDto();
        requestDto.setNombre("Fernando de la Mora");

        when(ciudadService.update(eq(1L), any(CiudadRequestDto.class))).thenReturn(ciudadResponseDto);

        // When & Then
        mockMvc.perform(put("/api/ciudades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Asunción")));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarCiudadYRetornar204() throws Exception {
        // Given: Para métodos void, usamos doNothing().
        doNothing().when(ciudadService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/ciudades/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
