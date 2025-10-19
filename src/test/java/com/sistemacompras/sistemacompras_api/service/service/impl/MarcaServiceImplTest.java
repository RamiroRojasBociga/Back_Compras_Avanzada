package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.MarcaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MarcaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Marca;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.MarcaMapper;
import com.sistemacompras.sistemacompras_api.repository.MarcaRepository;
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

// @ExtendWith(MockitoExtension.class): Activa la funcionalidad de Mockito en esta clase de test de JUnit 5.
@ExtendWith(MockitoExtension.class)
class MarcaServiceImplTest {

    // @Mock: Crea un objeto simulado (mock) para esta dependencia.
    // Esto nos permite controlar su comportamiento sin necesidad de una base de datos real.
    @Mock
    private MarcaRepository repository;

    @Mock
    private MarcaMapper mapper;

    // @InjectMocks: Crea una instancia real de MarcaServiceImpl e inyecta los mocks creados arriba.
    @InjectMocks
    private MarcaServiceImpl service;

    // --- Objetos de prueba ---
    private Marca marca;
    private MarcaRequestDto requestDto;
    private MarcaResponseDto responseDto;

    // @BeforeEach: Este método se ejecuta antes de cada test.
    // Prepara un estado inicial limpio para cada prueba.
    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Sony");

        requestDto = new MarcaRequestDto();
        requestDto.setNombre("Sony");

        responseDto = new MarcaResponseDto();
        responseDto.setId(1L);
        responseDto.setNombre("Sony");
    }

    // --- Tests del método create() ---
    @Test
    void create_DebeCrearMarcaExitosamente() {
        // Given (Dado): Configuramos los mocks.
        when(repository.existsByNombreIgnoreCase("Sony")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(marca);
        when(repository.save(any(Marca.class))).thenReturn(marca);
        when(mapper.toResponse(marca)).thenReturn(responseDto);

        // When (Cuando): Ejecutamos el método.
        MarcaResponseDto result = service.create(requestDto);

        // Then (Entonces): Verificamos el resultado.
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Sony");
        verify(repository).save(any(Marca.class));
    }

    @Test
    void create_DebeLanzarExcepcionSiNombreExiste() {
        // Given: Simulamos que la marca ya existe.
        when(repository.existsByNombreIgnoreCase("Sony")).thenReturn(true);

        // When & Then: Verificamos que se lanza la excepción correcta.
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre de la marca ya existe");

        verify(repository, never()).save(any(Marca.class));
    }

    // --- Tests para métodos de lectura ---
    @Test
    void findAll_DebeRetornarTodasLasMarcas() {
        // Given
        when(repository.findAll()).thenReturn(List.of(marca));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        // When
        List<MarcaResponseDto> result = service.findAll();

        // Then
        assertThat(result).isNotNull().hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void findById_DebeRetornarMarcaSiExiste() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(marca));
        when(mapper.toResponse(marca)).thenReturn(responseDto);

        // When
        MarcaResponseDto result = service.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    // --- Tests para update() y delete() ---
    @Test
    void update_DebeActualizarMarca() {
        // Given
        Long marcaId = 1L;
        MarcaRequestDto updateDto = new MarcaRequestDto();
        updateDto.setNombre("Samsung");

        when(repository.findById(marcaId)).thenReturn(Optional.of(marca));
        when(repository.existsByNombreIgnoreCase("Samsung")).thenReturn(false);
        when(repository.save(any(Marca.class))).thenReturn(marca);

        // When
        service.update(marcaId, updateDto);

        // Then
        verify(mapper).updateEntityFromRequest(updateDto, marca);
        verify(repository).save(marca);
    }

    @Test
    void delete_DebeEliminarMarca() {
        // Given
        Long marcaId = 1L;
        when(repository.existsById(marcaId)).thenReturn(true);

        // When
        service.delete(marcaId);

        // Then
        verify(repository).deleteById(marcaId);
    }
}
