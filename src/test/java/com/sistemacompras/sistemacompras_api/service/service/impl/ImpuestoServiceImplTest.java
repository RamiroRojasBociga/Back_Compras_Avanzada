package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.ImpuestoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ImpuestoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Impuesto;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.ImpuestoMapper;
import com.sistemacompras.sistemacompras_api.repository.ImpuestoRepository;

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
// Permite crear mocks (@Mock) e inyectarlos (@InjectMocks) para aislar la clase que se está probando.
@ExtendWith(MockitoExtension.class)
class ImpuestoServiceImplTest {

    // @Mock: Crea un objeto simulado (mock) para una dependencia.
    // En este caso, simulamos el repositorio para no depender de una base de datos real.
    @Mock
    private ImpuestoRepository repository;

    @Mock
    private ImpuestoMapper mapper;

    // @InjectMocks: Crea una instancia real de la clase a probar (ImpuestoServiceImpl)
    // e inyecta los mocks definidos anteriormente en ella.
    @InjectMocks
    private ImpuestoServiceImpl service;

    // --- Variables de prueba ---
    private Impuesto impuesto;
    private ImpuestoRequestDto requestDto;
    private ImpuestoResponseDto responseDto;

    // @BeforeEach: Este método se ejecuta antes de cada test.
    // Garantiza que cada prueba comience con un estado limpio y predecible.
    @BeforeEach
    void setUp() {
        // Objeto de entidad de dominio
        impuesto = new Impuesto();
        impuesto.setId(1L);
        impuesto.setNombre("IVA");
        impuesto.setPorcentaje(10.0f);

        // DTO para peticiones (crear/actualizar)
        requestDto = new ImpuestoRequestDto();
        requestDto.setNombre("IVA");
        requestDto.setPorcentaje(10.0f);

        // DTO para respuestas del servicio
        responseDto = new ImpuestoResponseDto();
        responseDto.setId(1L);
        responseDto.setNombre("IVA");
        responseDto.setPorcentaje(10.0f);
    }

    // --- Tests para el método create() ---
    @Test
    void create_DebeCrearImpuestoExitosamente() {
        // Given (Dado): Configuramos los mocks para el "camino feliz".
        when(repository.existsByNombreIgnoreCase("IVA")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(impuesto);
        when(repository.save(any(Impuesto.class))).thenReturn(impuesto);
        when(mapper.toResponse(impuesto)).thenReturn(responseDto);

        // When (Cuando): Ejecutamos la acción a probar.
        ImpuestoResponseDto result = service.create(requestDto);

        // Then (Entonces): Verificamos los resultados.
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("IVA");
        assertThat(result.getPorcentaje()).isEqualTo(10.0f);

        // Verificamos que el método save() del mock se haya llamado una vez.
        verify(repository, times(1)).save(any(Impuesto.class));
    }

    @Test
    void create_DebeLanzarExcepcionSiNombreYaExiste() {
        // Given: Simula que un impuesto con el mismo nombre ya existe.
        when(repository.existsByNombreIgnoreCase("IVA")).thenReturn(true);

        // When & Then: Verificamos que se lanza la excepción correcta.
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya existe");

        // Verificamos que el método save() NUNCA se llamó.
        verify(repository, never()).save(any(Impuesto.class));
    }

    // --- Tests para métodos de lectura ---
    @Test
    void findAll_DebeRetornarListaDeImpuestos() {
        // Given
        when(repository.findAll()).thenReturn(List.of(impuesto));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        // When
        List<ImpuestoResponseDto> result = service.findAll();

        // Then
        assertThat(result).isNotNull().hasSize(1);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_DebeRetornarImpuestoCuandoExiste() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(impuesto));
        when(mapper.toResponse(impuesto)).thenReturn(responseDto);

        // When
        ImpuestoResponseDto result = service.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    // --- Tests para métodos de modificación y eliminación ---
    @Test
    void update_DebeActualizarImpuesto() {
        // Given
        Long impuestoId = 1L;
        ImpuestoRequestDto updateDto = new ImpuestoRequestDto();
        updateDto.setNombre("IVA Actualizado");

        when(repository.findById(impuestoId)).thenReturn(Optional.of(impuesto));
        when(repository.existsByNombreIgnoreCase("IVA Actualizado")).thenReturn(false);
        when(repository.save(any(Impuesto.class))).thenReturn(impuesto);

        // When
        service.update(impuestoId, updateDto);

        // Then
        verify(mapper).updateEntityFromRequest(updateDto, impuesto);
        verify(repository).save(impuesto);
    }

    @Test
    void delete_DebeEliminarImpuesto() {
        // Given
        Long impuestoId = 1L;
        when(repository.existsById(impuestoId)).thenReturn(true);

        // When
        service.delete(impuestoId);

        // Then
        verify(repository).deleteById(impuestoId);
    }
}
