package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.CiudadRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CiudadResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Ciudad;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.CiudadMapper;
import com.sistemacompras.sistemacompras_api.repository.CiudadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class): Habilita el uso de Mockito en JUnit 5.
// Esto permite crear y gestionar mocks de forma automática.
@ExtendWith(MockitoExtension.class)
class CiudadServiceImplTest {

    // @Mock: Crea un "doble de prueba" o simulacro de una dependencia.
    // En lugar de usar la base de datos real, este mock simulará las respuestas del repositorio.
    @Mock
    private CiudadRepository repository;

    @Mock
    private CiudadMapper mapper;

    // @InjectMocks: Crea una instancia real de la clase que estamos probando (CiudadServiceImpl).
    // Automáticamente inyecta los mocks (@Mock) en esta instancia.
    @InjectMocks
    private CiudadServiceImpl service;

    // --- Variables de prueba ---
    private Ciudad ciudad;
    private CiudadRequestDto requestDto;
    private CiudadResponseDto responseDto;

    // @BeforeEach: Este método se ejecuta antes de CADA test.
    // Es ideal para inicializar objetos comunes y asegurar que cada test empiece con un estado limpio.
    @BeforeEach
    void setUp() {
        // Datos de prueba para una entidad Ciudad
        ciudad = new Ciudad();
        ciudad.setId(1L);
        ciudad.setNombre("Asunción");

        // DTO para las peticiones de creación/actualización
        requestDto = new CiudadRequestDto();
        requestDto.setNombre("Asunción");

        // DTO para las respuestas del servicio
        responseDto = new CiudadResponseDto();
        responseDto.setId(1L);
        responseDto.setNombre("Asunción");
    }

    @Test
    void create_DebeCrearCiudadCorrectamente() {
        // --- Given (Dado) ---
        // Configuramos el comportamiento de nuestros mocks.
        // 1. Simula que no existe una ciudad con ese nombre.
        when(repository.existsByNombreIgnoreCase(requestDto.getNombre())).thenReturn(false);
        // 2. Simula la conversión de DTO a Entidad.
        when(mapper.toEntity(requestDto)).thenReturn(ciudad);
        // 3. Simula la operación de guardado en la base de datos.
        when(repository.save(any(Ciudad.class))).thenReturn(ciudad);
        // 4. Simula la conversión de Entidad a DTO de respuesta.
        when(mapper.toResponse(ciudad)).thenReturn(responseDto);

        // --- When (Cuando) ---
        // Ejecutamos el método que queremos probar.
        CiudadResponseDto result = service.create(requestDto);

        // --- Then (Entonces) ---
        // Verificamos que los resultados son los esperados.
        // 1. El resultado no debe ser nulo.
        assertThat(result).isNotNull();
        // 2. El nombre de la ciudad creada debe ser el esperado.
        assertThat(result.getNombre()).isEqualTo("Asunción");

        // verify(...): Comprueba que ciertos métodos de los mocks fueron llamados.
        // Aquí, nos aseguramos de que el método save() se llamó exactamente una vez.
        verify(repository, times(1)).save(any(Ciudad.class));
    }

    @Test
    void create_DebeLanzarExcepcionSiNombreYaExiste() {
        // Given: Simula que la ciudad ya existe.
        when(repository.existsByNombreIgnoreCase(requestDto.getNombre())).thenReturn(true);

        // When & Then: Verificamos que al llamar a create(), se lanza la excepción correcta.
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya existe");

        // verify(..., never()): Nos aseguramos de que el método save() NUNCA se llamó.
        verify(repository, never()).save(any(Ciudad.class));
    }

    @Test
    void findById_DebeRetornarCiudadSiExiste() {
        // Given: Simula que el repositorio encuentra la ciudad.
        when(repository.findById(1L)).thenReturn(Optional.of(ciudad));
        when(mapper.toResponse(ciudad)).thenReturn(responseDto);

        // When: Llamamos al método.
        CiudadResponseDto result = service.findById(1L);

        // Then: Verificamos el resultado.
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_DebeLanzarExcepcionSiNoExiste() {
        // Given: Simula que el repositorio NO encuentra la ciudad.
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then: Verificamos que se lanza la excepción correcta.
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no encontrada");
    }
}
