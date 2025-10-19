package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaResponseDto;
import com.sistemacompras.sistemacompras_api.service.UnidadMedidaService;

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

@WebMvcTest(controllers = UnidadMedidaController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class UnidadMedidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UnidadMedidaService unidadMedidaService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private UnidadMedidaResponseDto unidadMedidaResponseDto;

    @BeforeEach
    void setUp() {
        // Usamos la definición correcta del DTO de respuesta
        unidadMedidaResponseDto = new UnidadMedidaResponseDto();
        unidadMedidaResponseDto.setId(1L);
        unidadMedidaResponseDto.setNombre("Kilogramo");
    }

    @Test
    @WithMockUser
    void list_DebeRetornarListaDeUnidadesDeMedida() throws Exception {
        when(unidadMedidaService.findAll()).thenReturn(List.of(unidadMedidaResponseDto));

        mockMvc.perform(get("/api/unidades_medida"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Kilogramo")));
    }

    @Test
    @WithMockUser
    void get_DebeRetornarUnidadDeMedidaPorId() throws Exception {
        when(unidadMedidaService.findById(1L)).thenReturn(unidadMedidaResponseDto);

        mockMvc.perform(get("/api/unidades_medida/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Kilogramo")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevaUnidadDeMedidaYRetornar201() throws Exception {
        UnidadMedidaRequestDto requestDto = new UnidadMedidaRequestDto();
        requestDto.setNombre("Litro");

        when(unidadMedidaService.create(any(UnidadMedidaRequestDto.class))).thenReturn(unidadMedidaResponseDto);

        mockMvc.perform(post("/api/unidades_medida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Kilogramo")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarUnidadDeMedidaExistente() throws Exception {
        UnidadMedidaRequestDto requestDto = new UnidadMedidaRequestDto();
        requestDto.setNombre("Metro");

        UnidadMedidaResponseDto responseActualizada = new UnidadMedidaResponseDto();
        responseActualizada.setId(1L);
        responseActualizada.setNombre("Metro");

        when(unidadMedidaService.update(eq(1L), any(UnidadMedidaRequestDto.class))).thenReturn(responseActualizada);

        mockMvc.perform(put("/api/unidades_medida/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Metro")));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarUnidadDeMedidaYRetornar204() throws Exception {
        doNothing().when(unidadMedidaService).delete(1L);

        mockMvc.perform(delete("/api/unidades_medida/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
