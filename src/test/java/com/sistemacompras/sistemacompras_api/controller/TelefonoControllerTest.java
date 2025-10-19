package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.TelefonoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.TelefonoResponseDto;
import com.sistemacompras.sistemacompras_api.service.TelefonoService;

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

@WebMvcTest(controllers = TelefonoController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class TelefonoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TelefonoService telefonoService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private TelefonoResponseDto telefonoResponseDto;

    @BeforeEach
    void setUp() {
        telefonoResponseDto = new TelefonoResponseDto();
        telefonoResponseDto.setId(1L);
        telefonoResponseDto.setNumero("0981123456");
    }

    @Test
    @WithMockUser
    void list_DebeRetornarListaDeTelefonos() throws Exception {
        when(telefonoService.findAll()).thenReturn(List.of(telefonoResponseDto));

        mockMvc.perform(get("/api/Telefonos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numero", is("0981123456")));
    }

    @Test
    @WithMockUser
    void get_DebeRetornarTelefonoPorId() throws Exception {
        when(telefonoService.findById(1L)).thenReturn(telefonoResponseDto);

        mockMvc.perform(get("/api/Telefonos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.numero", is("0981123456")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevoTelefonoYRetornar201() throws Exception {
        // --- CÓDIGO CORREGIDO ---
        // El DTO solo contiene el número, como en tu diseño original.
        TelefonoRequestDto requestDto = new TelefonoRequestDto();
        requestDto.setNumero("0971654321");

        when(telefonoService.create(any(TelefonoRequestDto.class))).thenReturn(telefonoResponseDto);

        mockMvc.perform(post("/api/Telefonos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero", is("0981123456")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarTelefonoExistente() throws Exception {
        // --- CÓDIGO CORREGIDO ---
        // El DTO solo contiene el número.
        TelefonoRequestDto requestDto = new TelefonoRequestDto();
        requestDto.setNumero("0994999888");

        TelefonoResponseDto responseActualizado = new TelefonoResponseDto();
        responseActualizado.setId(1L);
        responseActualizado.setNumero("0994999888");

        when(telefonoService.update(eq(1L), any(TelefonoRequestDto.class))).thenReturn(responseActualizado);

        mockMvc.perform(put("/api/Telefonos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero", is("0994999888")));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarTelefonoYRetornar204() throws Exception {
        doNothing().when(telefonoService).delete(1L);

        mockMvc.perform(delete("/api/Telefonos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
