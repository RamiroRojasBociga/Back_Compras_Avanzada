package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.ImpuestoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ImpuestoResponseDto;
import com.sistemacompras.sistemacompras_api.service.ImpuestoService;

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

// 1. @WebMvcTest: Enfocado solo en ImpuestoController.
@WebMvcTest(controllers = ImpuestoController.class,
        // Excluimos el filtro JWT para simplificar el test.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class ImpuestoControllerTest {

    // 2. Herramientas inyectadas por Spring.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 3. Mocks para las dependencias del contexto.
    @MockitoBean
    private ImpuestoService impuestoService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    // Objeto de prueba para reutilizar.
    private ImpuestoResponseDto impuestoResponseDto;

    @BeforeEach
    void setUp() {
        impuestoResponseDto = new ImpuestoResponseDto();
        impuestoResponseDto.setId(1L);
        impuestoResponseDto.setNombre("IVA");
        impuestoResponseDto.setPorcentaje(10.0f);
    }

    // 4. @WithMockUser: Simula un usuario autenticado para pasar la seguridad.
    @Test
    @WithMockUser
    void list_DebeRetornarListaDeImpuestos() throws Exception {
        // Given
        when(impuestoService.findAll()).thenReturn(List.of(impuestoResponseDto));

        // When & Then
        mockMvc.perform(get("/api/Impuestos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("IVA")));
    }

    @Test
    @WithMockUser
    void get_DebeRetornarImpuestoPorId() throws Exception {
        // Given
        when(impuestoService.findById(1L)).thenReturn(impuestoResponseDto);

        // When & Then
        mockMvc.perform(get("/api/Impuestos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("IVA")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevoImpuestoYRetornar201() throws Exception {
        // Given
        ImpuestoRequestDto requestDto = new ImpuestoRequestDto();
        requestDto.setNombre("IVA");
        requestDto.setPorcentaje(10.0f);

        when(impuestoService.create(any(ImpuestoRequestDto.class))).thenReturn(impuestoResponseDto);

        // When & Then
        mockMvc.perform(post("/api/Impuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        // 5. .with(csrf()): Necesario para peticiones que modifican datos.
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("IVA")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarImpuestoExistente() throws Exception {
        // Given
        ImpuestoRequestDto requestDto = new ImpuestoRequestDto();
        requestDto.setNombre("ISE");
        requestDto.setPorcentaje(5.0f);

        ImpuestoResponseDto responseActualizado = new ImpuestoResponseDto();
        responseActualizado.setId(1L);
        responseActualizado.setNombre("ISE");
        responseActualizado.setPorcentaje(5.0f);

        when(impuestoService.update(eq(1L), any(ImpuestoRequestDto.class))).thenReturn(responseActualizado);

        // When & Then
        mockMvc.perform(put("/api/Impuestos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("ISE")));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarImpuestoYRetornar204() throws Exception {
        // Given
        doNothing().when(impuestoService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/Impuestos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
