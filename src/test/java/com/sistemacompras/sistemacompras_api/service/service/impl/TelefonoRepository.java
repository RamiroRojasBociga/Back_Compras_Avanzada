package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.TelefonoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.TelefonoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.TelefonoMapper;
import com.sistemacompras.sistemacompras_api.repository.TelefonoRepository;

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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class): Habilita el uso de Mockito en JUnit 5.
// Esto permite crear "dobles de prueba" (mocks) para aislar la clase que estamos probando.
@ExtendWith(MockitoExtension.class)
class TelefonoServiceImplTest {

    // @Mock: Crea un objeto simulado (mock) para una dependencia. En este caso, simulamos
    // el repositorio para no depender de una base de datos real durante los tests unitarios.
    @Mock
    private TelefonoRepository repository;

    @Mock
    private TelefonoMapper mapper;

    // @InjectMocks: Crea una instancia real de la clase a probar (TelefonoServiceImpl)
    // e inyecta automáticamente los mocks (@Mock) en ella.
    @InjectMocks
    private TelefonoServiceImpl service;

    // --- Objetos de prueba ---
    private Telefono telefono;
    private TelefonoRequestDto requestDto;
    private TelefonoResponseDto responseDto;

    // @BeforeEach: Este método se ejecuta antes de cada test.
    // Prepara un estado inicial limpio para que cada prueba sea independiente y predecible.
    @BeforeEach
    void setUp() {
        telefono = new Telefono();
        telefono.setId(1L);
        telefono.setNumero("0981123456");

        requestDto = new TelefonoRequestDto();
        requestDto.setNumero("0981123456");

        responseDto = new TelefonoResponseDto();
        responseDto.setId(1L);
        responseDto.setNumero("0981123456");
    }

    // --- Test para el método create() ---
    @Test
    void create_DebeCrearTelefonoExitosamente() {
        // Given (Dado): Configuramos los mocks para el escenario de éxito.
        when(repository.existsByNumeroIgnoreCase("0981123456")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(telefono);
        when(repository.save(any(Telefono.class))).thenReturn(telefono);
        when(mapper.toResponse(telefono)).thenReturn(responseDto);

        // When (Cuando): Ejecutamos la acción a probar.
        TelefonoResponseDto result = service.create(requestDto);

        // Then (Entonces): Verificamos los resultados.
        assertThat(result).isNotNull();
        assertThat(result.getNumero()).isEqualTo("0981123456");
        verify(repository).save(any(Telefono.class));
    }

    // --- Test para el caso de error en create() ---
    @Test
    void create_DebeLanzarExcepcionSiNumeroYaExiste() {
        // Given: Simulamos que el número de teléfono ya existe.
        when(repository.existsByNumeroIgnoreCase("0981123456")).thenReturn(true);

        // When & Then: Verificamos que se lanza la excepción correcta.
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El numero de telefono ya existe");

        // Verificamos que el método save() NUNCA se llamó.
        verify(repository, never()).save(any(Telefono.class));
    }

    // --- Tests para los métodos de lectura (find) ---
    @Test
    void findAll_DebeRetornarTodosLosTelefonos() {
        when(repository.findAll()).thenReturn(List.of(telefono));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        List<TelefonoResponseDto> result = service.findAll();

        assertThat(result).isNotNull().hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void findById_DebeRetornarTelefonoSiExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(telefono));
        when(mapper.toResponse(telefono)).thenReturn(responseDto);

        TelefonoResponseDto result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    // --- Tests para update() y delete() ---
    @Test
    void update_DebeActualizarTelefono() {
        Long telefonoId = 1L;
        TelefonoRequestDto updateDto = new TelefonoRequestDto();
        updateDto.setNumero("0991654321");

        when(repository.findById(telefonoId)).thenReturn(Optional.of(telefono));
        when(repository.existsByNumeroIgnoreCase("0991654321")).thenReturn(false);
        when(repository.save(any(Telefono.class))).thenReturn(telefono);

        service.update(telefonoId, updateDto);

        verify(mapper).updateEntityFromRequest(updateDto, telefono);
        verify(repository).save(telefono);
    }

    @Test
    void delete_DebeEliminarTelefono() {
        Long telefonoId = 1L;
        when(repository.existsById(telefonoId)).thenReturn(true);

        service.delete(telefonoId);

        verify(repository).deleteById(telefonoId);
    }
}
