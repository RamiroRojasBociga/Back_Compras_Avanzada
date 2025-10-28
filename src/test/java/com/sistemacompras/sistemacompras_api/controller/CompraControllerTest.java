package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;
import com.sistemacompras.sistemacompras_api.service.CompraService;
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

import java.time.LocalDate;
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

@WebMvcTest(controllers = CompraController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private CompraService compraService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private CompraResponseDto compraResponseDto;

    @BeforeEach
    void setUp() {
        compraResponseDto = new CompraResponseDto();
        compraResponseDto.setIdCompra(1L);
        compraResponseDto.setNombreUsuario("Test User");
        compraResponseDto.setFecha(LocalDate.now());
        compraResponseDto.setEstado(EstadoCompra.PENDIENTE); // ✅ Usar Enum directamente
        compraResponseDto.setNumFactura("FACT-2025-0001"); // ✅ Agregar numFactura
    }

    @Test
    @WithMockUser
    void list_DebeRetornarListaDeCompras() throws Exception {
        when(compraService.findAll()).thenReturn(List.of(compraResponseDto));

        mockMvc.perform(get("/api/compras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado", is("PENDIENTE"))); // ✅ JSON será String
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevaCompraYRetornar201() throws Exception {
        CompraRequestDto requestDto = new CompraRequestDto();
        requestDto.setIdUsuario(1L);
        requestDto.setIdProveedor(1L);
        requestDto.setFecha(LocalDate.now());
        requestDto.setEstado(EstadoCompra.PENDIENTE); // ✅ Usar Enum
        // numFactura puede ser null (se genera automáticamente)

        when(compraService.create(any(CompraRequestDto.class))).thenReturn(compraResponseDto);

        mockMvc.perform(post("/api/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado", is("PENDIENTE"))); // ✅ JSON será String
    }

    @Test
    @WithMockUser
    void update_DebeActualizarCompraExistente() throws Exception {
        CompraRequestDto requestDto = new CompraRequestDto();
        requestDto.setIdUsuario(1L);
        requestDto.setIdProveedor(1L);
        requestDto.setFecha(LocalDate.now());
        requestDto.setEstado(EstadoCompra.PROCESADA); // ✅ CORREGIDO: Estado válido

        // Configurar response actualizada
        CompraResponseDto responseActualizada = new CompraResponseDto();
        responseActualizada.setIdCompra(1L);
        responseActualizada.setEstado(EstadoCompra.PROCESADA); // ✅ Usar Enum
        responseActualizada.setNumFactura("FACT-2025-0001");

        when(compraService.update(eq(1L), any(CompraRequestDto.class))).thenReturn(responseActualizada);

        mockMvc.perform(put("/api/compras/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("PROCESADA"))); // ✅ JSON será String
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarCompraYRetornar204() throws Exception {
        doNothing().when(compraService).delete(1L);

        mockMvc.perform(delete("/api/compras/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // ✅ NUEVO TEST: Para endpoints específicos de estado
    @Test
    @WithMockUser
    void getByEstado_DebeRetornarComprasPorEstado() throws Exception {
        when(compraService.findByEstado(EstadoCompra.PENDIENTE)).thenReturn(List.of(compraResponseDto));

        mockMvc.perform(get("/api/compras/estado/PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado", is("PENDIENTE")));
    }

    // ✅ NUEVO TEST: Para cambiar estado
    @Test
    @WithMockUser
    void cambiarEstado_DebeActualizarEstadoCompra() throws Exception {
        doNothing().when(compraService).cambiarEstado(1L, EstadoCompra.PROCESADA);

        mockMvc.perform(patch("/api/compras/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"PROCESADA\"") // ✅ Enviar como string JSON
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}