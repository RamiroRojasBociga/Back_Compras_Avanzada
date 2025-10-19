package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.CategoriaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CategoriaResponseDto;
import com.sistemacompras.sistemacompras_api.service.CategoriaService;

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
import org.springframework.security.test.context.support.WithMockUser; // Para simular un usuario logueado
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

// 1. @WebMvcTest: Enfocado solo en CategoriaController y la capa web.
@WebMvcTest(controllers = CategoriaController.class,
        // Excluimos el filtro JWT para no tener que generar un token real en los tests.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class CategoriaControllerTest {

    // 2. Herramientas inyectadas por Spring para los tests.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 3. Mocks para TODAS las dependencias del contexto de seguridad y del controlador.
    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    // Objeto de prueba que usaremos en varios tests.
    private CategoriaResponseDto categoriaResponseDto;

    @BeforeEach
    void setUp() {
        categoriaResponseDto = new CategoriaResponseDto();
        categoriaResponseDto.setId(1L);
        categoriaResponseDto.setNombre("Electrónica");
    }

    // 4. @WithMockUser: Simula que la petición es hecha por un usuario autenticado.
    // Esto es crucial para pasar los filtros de seguridad de Spring Security.
    @Test
    @WithMockUser
    void list_DebeRetornarListaDeCategorias() throws Exception {
        // Given: Configuramos el mock del servicio.
        when(categoriaService.findAll()).thenReturn(List.of(categoriaResponseDto));

        // When & Then: Realizamos la petición y verificamos el resultado.
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))) // Verifica que la lista JSON tiene 1 elemento.
                .andExpect(jsonPath("$[0].nombre", is("Electrónica"))); // Verifica el nombre del primer elemento.
    }

    @Test
    @WithMockUser
    void get_DebeRetornarCategoriaPorId() throws Exception {
        // Given
        when(categoriaService.findById(1L)).thenReturn(categoriaResponseDto);

        // When & Then
        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Electrónica")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevaCategoriaYRetornar201() throws Exception {
        // Given
        CategoriaRequestDto requestDto = new CategoriaRequestDto();
        requestDto.setNombre("Hogar");

        when(categoriaService.create(any(CategoriaRequestDto.class))).thenReturn(categoriaResponseDto);

        // When & Then
        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        // 5. .with(csrf()): Necesario para peticiones POST, PUT, DELETE para pasar la protección CSRF.
                        .with(csrf()))
                .andExpect(status().isCreated()) // Verifica el código 201 Created.
                .andExpect(jsonPath("$.nombre", is("Electrónica")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarCategoriaExistente() throws Exception {
        // Given
        CategoriaRequestDto requestDto = new CategoriaRequestDto();
        requestDto.setNombre("Tecnología");

        when(categoriaService.update(eq(1L), any(CategoriaRequestDto.class))).thenReturn(categoriaResponseDto);

        // When & Then
        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Electrónica")));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarCategoriaYRetornar204() throws Exception {
        // Given: Para un método void, usamos doNothing().
        doNothing().when(categoriaService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/categorias/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Verifica el código 204 No Content.
    }
}
