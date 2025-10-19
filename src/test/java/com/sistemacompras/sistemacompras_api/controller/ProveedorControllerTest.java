package com.sistemacompras.sistemacompras_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.service.ProveedorService;
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

@WebMvcTest(controllers = ProveedorController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProveedorService proveedorService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private ProveedorResponseDto proveedorResponseDto;

    @BeforeEach
    void setUp() {
        // --- SOLUCIÓN: Creamos el objeto DTO con la estructura correcta ---
        proveedorResponseDto = new ProveedorResponseDto();
        proveedorResponseDto.setIdProveedor(1L); // Nombre de método correcto
        proveedorResponseDto.setNombre("TechGlobal S.A.");
        proveedorResponseDto.setIdCiudad(10L);
        proveedorResponseDto.setNombreCiudad("Asunción");
        proveedorResponseDto.setDireccion("Av. Principal 123");
        proveedorResponseDto.setEmail("contacto@techglobal.com");
        proveedorResponseDto.setEstado("ACTIVO");
    }

    @Test
    @WithMockUser
    void list_DebeRetornarListaDeProveedores() throws Exception {
        when(proveedorService.findAll()).thenReturn(List.of(proveedorResponseDto));

        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                // --- SOLUCIÓN: Verificamos con los campos correctos ---
                .andExpect(jsonPath("$[0].nombre", is("TechGlobal S.A.")))
                .andExpect(jsonPath("$[0].email", is("contacto@techglobal.com")));
    }

    @Test
    @WithMockUser
    void get_DebeRetornarProveedorPorId() throws Exception {
        when(proveedorService.findById(1L)).thenReturn(proveedorResponseDto);

        mockMvc.perform(get("/api/proveedores/1"))
                .andExpect(status().isOk())
                // --- SOLUCIÓN: Usamos los nombres de campo correctos en el JSONPath ---
                .andExpect(jsonPath("$.idProveedor", is(1)))
                .andExpect(jsonPath("$.nombre", is("TechGlobal S.A.")))
                .andExpect(jsonPath("$.nombreCiudad", is("Asunción")));
    }

    @Test
    @WithMockUser
    void create_DebeCrearNuevoProveedorYRetornar201() throws Exception {
        // Asumimos que el RequestDto tiene campos similares para pasar la validación
        ProveedorRequestDto requestDto = new ProveedorRequestDto();
        requestDto.setNombre("Importadora Digital");
        requestDto.setIdCiudad(5L);
        requestDto.setDireccion("Calle Falsa 456");
        requestDto.setEmail("ventas@digital.com");
        requestDto.setEstado("ACTIVO");

        when(proveedorService.create(any(ProveedorRequestDto.class))).thenReturn(proveedorResponseDto);

        mockMvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("TechGlobal S.A.")));
    }

    @Test
    @WithMockUser
    void update_DebeActualizarProveedorExistente() throws Exception {
        ProveedorRequestDto requestDto = new ProveedorRequestDto();
        requestDto.setNombre("Soluciones Informáticas Paraguay");
        requestDto.setIdCiudad(10L);
        requestDto.setDireccion("Nueva Dirección 789");
        requestDto.setEmail("info@soluciones.py");
        requestDto.setEstado("INACTIVO");

        ProveedorResponseDto responseActualizado = new ProveedorResponseDto();
        responseActualizado.setIdProveedor(1L);
        responseActualizado.setNombre("Soluciones Informáticas Paraguay");

        when(proveedorService.update(eq(1L), any(ProveedorRequestDto.class))).thenReturn(responseActualizado);

        mockMvc.perform(put("/api/proveedores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Soluciones Informáticas Paraguay")));
    }

    @Test
    @WithMockUser
    void delete_DebeEliminarProveedorYRetornar204() throws Exception {
        doNothing().when(proveedorService).delete(1L);

        mockMvc.perform(delete("/api/proveedores/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
