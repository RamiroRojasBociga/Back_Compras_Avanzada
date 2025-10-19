package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.MarcaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MarcaResponseDto;
import com.sistemacompras.sistemacompras_api.service.MarcaService;

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

// 1. @WebMvcTest: Enfocado solo en MarcaController.
@WebMvcTest(controllers = MarcaController.class,
        // Excluimos el filtro JWT para simplificar el test.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class MarcaControllerTest {

    // 2. Herramientas inyectadas por Spring.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 3. Mocks para las dependencias del contexto.
    @MockitoBean
    private MarcaService marcaService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    // Objeto de prueba para reutilizar.
    private MarcaResponseDto marcaResponseDto;

    @BeforeEach
    void setUp() {
        marcaResponseDto = new MarcaResponseDto();
        marcaResponseDto.setId(1L);
        marcaResponseDto.setNombre("Samsung");
    }

    // 4. @WithMockUser: Simula un usuario autenticado para pasar la seguridad.
    @Test
    @WithMockUser
    void list_DebeRetornarListaDeMarcas() throws Exception {
        // Given
        when(marcaService.findAll()).thenReturn(List.of(marcaResponseDto));

        // When & Then
        mockMvc.perform(get("/api/marcas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Samsung")));
    }

    @Test
    @WithMockUser
    void get_DebeRetornarMarcaPorId() throws Exception {
        // Given
        when(marcaService.findById(1L)).thenReturn(marcaResponseDto);

        // When & Then
        mockMvc.perform(get("/api/marcas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Samsung")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevaMarcaYRetornar201() throws Exception {
        // Given
        MarcaRequestDto requestDto = new MarcaRequestDto();
        requestDto.setNombre("Apple");

        when(marcaService.create(any(MarcaRequestDto.class))).thenReturn(marcaResponseDto);

        // When & Then
        mockMvc.perform(post("/api/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        // 5. .with(csrf()): Necesario para peticiones que modifican datos.
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Samsung")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarMarcaExistente() throws Exception {
        // Given
        MarcaRequestDto requestDto = new MarcaRequestDto();
        requestDto.setNombre("LG");

        MarcaResponseDto responseActualizada = new MarcaResponseDto();
        responseActualizada.setId(1L);
        responseActualizada.setNombre("LG");

        when(marcaService.update(eq(1L), any(MarcaRequestDto.class))).thenReturn(responseActualizada);

        // When & Then
        mockMvc.perform(put("/api/marcas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("LG")));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarMarcaYRetornar204() throws Exception {
        // Given
        doNothing().when(marcaService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/marcas/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
