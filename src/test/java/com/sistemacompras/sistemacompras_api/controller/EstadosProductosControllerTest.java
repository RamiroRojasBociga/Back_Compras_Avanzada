package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.config.JwtAuthenticationFilter;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

// Como este controlador es simple, no necesitamos los mocks de servicio,
// pero los dejo comentados para que recuerdes que los necesitarás en otros controladores.
// import com.sistemacompras.sistemacompras_api.config.JwtTokenProvider;
// import com.sistemacompras.sistemacompras_api.service.service.impl.CustomUserDetailsServiceImpl;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EstadosProductosController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class EstadosProductosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getEstadosProducto_DebeRetornarListaCompletaDeEstados() throws Exception {
        // No hay "Given" porque no hay mocks que configurar.

        // When & Then
        mockMvc.perform(get("/api/opciones/estados/producto"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))

                // --- SOLUCIÓN: Usamos los nombres de campo correctos 'valor' y 'label' ---

                // Verificamos el primer estado (ACTIVO)
                .andExpect(jsonPath("$[0].valor", is(EstadoProducto.ACTIVO.name())))
                .andExpect(jsonPath("$[0].label", is(EstadoProducto.ACTIVO.getDescripcion())))

                // Verificamos el segundo estado (INACTIVO)
                .andExpect(jsonPath("$[1].valor", is(EstadoProducto.INACTIVO.name())))
                .andExpect(jsonPath("$[1].label", is(EstadoProducto.INACTIVO.getDescripcion())))

                // Verificamos el tercer estado (AGOTADO)
                .andExpect(jsonPath("$[2].valor", is(EstadoProducto.AGOTADO.name())))
                .andExpect(jsonPath("$[2].label", is(EstadoProducto.AGOTADO.getDescripcion())))

                // Verificamos el cuarto estado (DESCONTINUADO)
                .andExpect(jsonPath("$[3].valor", is(EstadoProducto.DESCONTINUADO.name())))
                .andExpect(jsonPath("$[3].label", is(EstadoProducto.DESCONTINUADO.getDescripcion())));
    }
}
