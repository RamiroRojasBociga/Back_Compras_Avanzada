package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.service.DetalleCompraService;
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

@WebMvcTest(controllers = DetalleCompraController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class DetalleCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DetalleCompraService detalleCompraService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private DetalleCompraResponseDto detalleCompraResponseDto;

    @BeforeEach
    void setUp() {
        detalleCompraResponseDto = new DetalleCompraResponseDto();
        detalleCompraResponseDto.setIdDetalleCompra(1L);
        detalleCompraResponseDto.setIdProducto(100L);
        detalleCompraResponseDto.setNombreProducto("Teclado Mecánico");
        detalleCompraResponseDto.setCantidad(5);
    }

    @Test
    @WithMockUser
    void list_DebeRetornarListaDeDetalles() throws Exception {
        when(detalleCompraService.findAll()).thenReturn(List.of(detalleCompraResponseDto));

        mockMvc.perform(get("/api/DetalleCompras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cantidad", is(5)));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevoDetalleYRetornar201() throws Exception {
        // --- Creamos un DTO válido ---
        DetalleCompraRequestDto requestDto = new DetalleCompraRequestDto();
        requestDto.setIdCompra(1L);
        requestDto.setIdProducto(100L);
        requestDto.setCantidad(5);

        when(detalleCompraService.create(any(DetalleCompraRequestDto.class))).thenReturn(detalleCompraResponseDto);

        mockMvc.perform(post("/api/DetalleCompras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cantidad", is(5)));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarDetalleExistente() throws Exception {
        // --- SOLUCIÓN: Creamos un DTO de actualización válido ---
        DetalleCompraRequestDto requestDto = new DetalleCompraRequestDto();
        requestDto.setIdCompra(1L);      // Campo obligatorio
        requestDto.setIdProducto(100L);  // Campo obligatorio
        requestDto.setCantidad(10);      // El campo que queremos cambiar

        DetalleCompraResponseDto responseActualizado = new DetalleCompraResponseDto();
        responseActualizado.setIdDetalleCompra(1L);
        responseActualizado.setCantidad(10);

        when(detalleCompraService.update(eq(1L), any(DetalleCompraRequestDto.class))).thenReturn(responseActualizado);

        mockMvc.perform(put("/api/DetalleCompras/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk()) // Ahora sí debería ser 200 OK
                .andExpect(jsonPath("$.cantidad", is(10)));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarDetalleYRetornar204() throws Exception {
        doNothing().when(detalleCompraService).delete(1L);

        mockMvc.perform(delete("/api/DetalleCompras/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
