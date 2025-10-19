package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.UnidadMedida;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.UnidadMedidaMapper;
import com.sistemacompras.sistemacompras_api.repository.UnidadMedidaRepository;

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

// @ExtendWith(MockitoExtension.class): Habilita el uso de Mockito en esta clase de test de JUnit 5.
// Es la anotación que "enciende" la capacidad de crear y gestionar mocks.
@ExtendWith(MockitoExtension.class)
class UnidadMedidaServiceImplTest {

    // @Mock: Crea un objeto simulado (mock) para una dependencia.
    // Simulamos el repositorio y el mapper para aislar el servicio y no depender
    // de una base de datos real ni de la lógica de conversión real.
    @Mock
    private UnidadMedidaRepository repository;

    @Mock
    private UnidadMedidaMapper mapper;

    // @InjectMocks: Crea una instancia real de la clase que queremos probar (UnidadMedidaServiceImpl)
    // e inyecta automáticamente los mocks definidos arriba en ella.
    @InjectMocks
    private UnidadMedidaServiceImpl service;

    // --- Objetos de prueba ---
    private UnidadMedida unidadMedida;
    private UnidadMedidaRequestDto requestDto;
    private UnidadMedidaResponseDto responseDto;

    // @BeforeEach: Este método se ejecuta antes de cada test.
    // Garantiza que cada prueba comience con un estado limpio y consistente.
    @BeforeEach
    void setUp() {
        unidadMedida = new UnidadMedida();
        unidadMedida.setId(1L);
        unidadMedida.setNombre("Kilogramo");

        requestDto = new UnidadMedidaRequestDto();
        requestDto.setNombre("Kilogramo");

        responseDto = new UnidadMedidaResponseDto();
        responseDto.setId(1L);
        responseDto.setNombre("Kilogramo");
    }

    // --- Test para el método create() - Camino Feliz ---
    @Test
    void create_DebeCrearUnidadDeMedidaExitosamente() {
        // Given (Dado): Configuramos el comportamiento de nuestros mocks.
        when(repository.existsByNombreIgnoreCase("Kilogramo")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(unidadMedida);
        when(repository.save(any(UnidadMedida.class))).thenReturn(unidadMedida);
        when(mapper.toResponse(unidadMedida)).thenReturn(responseDto);

        // When (Cuando): Ejecutamos la acción que queremos probar.
        UnidadMedidaResponseDto result = service.create(requestDto);

        // Then (Entonces): Verificamos que el resultado es el esperado.
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Kilogramo");
        verify(repository).save(any(UnidadMedida.class)); // Verificamos que se intentó guardar.
    }

    // --- Test para el método create() - Caso de Error ---
    @Test
    void create_DebeLanzarExcepcionSiNombreYaExiste() {
        // Given: Simulamos que el nombre ya existe en la base de datos.
        when(repository.existsByNombreIgnoreCase("Kilogramo")).thenReturn(true);

        // When & Then: Verificamos que al llamar a create(), se lanza la excepción correcta con el mensaje esperado.
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre de la unidad de medida  ya existe");

        // Verificamos que el método save() NUNCA fue invocado, ya que la validación falló antes.
        verify(repository, never()).save(any(UnidadMedida.class));
    }

    // --- Tests para los métodos de lectura ---
    @Test
    void findAll_DebeRetornarTodasLasUnidadesDeMedida() {
        when(repository.findAll()).thenReturn(List.of(unidadMedida));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        List<UnidadMedidaResponseDto> result = service.findAll();

        assertThat(result).isNotNull().hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void findById_DebeRetornarUnidadDeMedidaSiExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(unidadMedida));
        when(mapper.toResponse(unidadMedida)).thenReturn(responseDto);

        UnidadMedidaResponseDto result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    // --- Tests para update() y delete() ---
    @Test
    void update_DebeActualizarUnidadDeMedida() {
        Long id = 1L;
        UnidadMedidaRequestDto updateDto = new UnidadMedidaRequestDto();
        updateDto.setNombre("Litro");

        when(repository.findById(id)).thenReturn(Optional.of(unidadMedida));
        when(repository.existsByNombreIgnoreCase("Litro")).thenReturn(false);
        when(repository.save(any(UnidadMedida.class))).thenReturn(unidadMedida);

        service.update(id, updateDto);

        verify(mapper).updateEntityFromRequest(updateDto, unidadMedida);
        verify(repository).save(unidadMedida);
    }

    @Test
    void delete_DebeEliminarUnidadDeMedida() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository).deleteById(id);
    }
}
