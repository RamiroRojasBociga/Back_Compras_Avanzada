package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import com.sistemacompras.sistemacompras_api.service.ProductoService;

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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private ProductoResponseDto productoResponseDto;

    @BeforeEach
    void setUp() {
        // Creamos un DTO de respuesta completo y realista
        productoResponseDto = new ProductoResponseDto();
        productoResponseDto.setIdProducto(1L);
        productoResponseDto.setNombre("Laptop Gamer Pro");
        productoResponseDto.setIdCategoria(10L);
        productoResponseDto.setNombreCategoria("Electrónica");
        productoResponseDto.setIdMarca(5L);
        productoResponseDto.setNombreMarca("TechCorp");
        productoResponseDto.setIdUnidadMedida(1L);
        productoResponseDto.setNombreUnidadMedida("Unidad");
        productoResponseDto.setCantidadUnidadesMedida(1);
        productoResponseDto.setIdImpuesto(2L);
        productoResponseDto.setNombreImpuesto("IVA 10%");
        productoResponseDto.setPorcentajeImpuesto(10.0f);
        productoResponseDto.setPrecio(new BigDecimal("1250.99"));
        productoResponseDto.setStock(50);
        productoResponseDto.setEstado(EstadoProducto.ACTIVO);
        productoResponseDto.setDescripcion(EstadoProducto.ACTIVO.getDescripcion());
    }

    @Test
    @WithMockUser
    void list_DebeRetornarListaDeProductos() throws Exception {
        when(productoService.findAll()).thenReturn(List.of(productoResponseDto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Laptop Gamer Pro")));
    }

    @Test
    @WithMockUser
    void get_DebeRetornarProductoPorId() throws Exception {
        when(productoService.findById(1L)).thenReturn(productoResponseDto);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto", is(1)))
                .andExpect(jsonPath("$.nombreCategoria", is("Electrónica")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevoProductoYRetornar201() throws Exception {
        // Creamos un DTO de solicitud válido y completo para pasar la validación @Valid
        ProductoRequestDto requestDto = new ProductoRequestDto();
        requestDto.setNombre("Nuevo Monitor 4K");
        requestDto.setIdCategoria(10L);
        requestDto.setIdMarca(5L);
        requestDto.setIdUnidadMedida(1L);
        requestDto.setIdImpuesto(2L);
        requestDto.setCantidadUnidadesMedida(1);
        requestDto.setPrecio(new BigDecimal("499.99"));
        requestDto.setStock(100);
        requestDto.setEstado(EstadoProducto.ACTIVO);

        when(productoService.create(any(ProductoRequestDto.class))).thenReturn(productoResponseDto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Laptop Gamer Pro")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarProductoExistente() throws Exception {
        ProductoRequestDto requestDto = new ProductoRequestDto();
        requestDto.setNombre("Laptop Gamer Pro v2");
        // Rellenamos todos los campos obligatorios para que el DTO sea válido
        requestDto.setIdCategoria(10L);
        requestDto.setIdMarca(5L);
        requestDto.setIdUnidadMedida(1L);
        requestDto.setIdImpuesto(2L);
        requestDto.setCantidadUnidadesMedida(1);
        requestDto.setPrecio(new BigDecimal("1300.00"));
        requestDto.setStock(40);
        requestDto.setEstado(EstadoProducto.ACTIVO);

        ProductoResponseDto responseActualizado = new ProductoResponseDto();
        responseActualizado.setIdProducto(1L);
        responseActualizado.setNombre("Laptop Gamer Pro v2");

        when(productoService.update(eq(1L), any(ProductoRequestDto.class))).thenReturn(responseActualizado);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Laptop Gamer Pro v2")));
    }

    @Test
    @WithMockUser
    void getProductosPorEstado_DebeRetornarListaCorrecta() throws Exception {
        when(productoService.findByEstado(EstadoProducto.INACTIVO)).thenReturn(List.of(productoResponseDto));

        mockMvc.perform(get("/api/productos/estado/{estado}", "INACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    void cambiarEstadoProducto_DebeLlamarAlServicioYRetornar200() throws Exception {
        doNothing().when(productoService).cambiarEstado(eq(1L), eq(EstadoProducto.DESCONTINUADO));

        Map<String, String> requestBody = Map.of("estado", "DESCONTINUADO");

        mockMvc.perform(patch("/api/productos/{id}/estado", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarProductoYRetornar204() throws Exception {
        doNothing().when(productoService).delete(1L);

        mockMvc.perform(delete("/api/productos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
