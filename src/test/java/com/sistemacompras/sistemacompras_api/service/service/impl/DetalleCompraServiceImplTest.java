package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.DetalleCompra;
import com.sistemacompras.sistemacompras_api.entity.Compra;
import com.sistemacompras.sistemacompras_api.entity.Producto;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.DetalleCompraMapper;
import com.sistemacompras.sistemacompras_api.repository.DetalleCompraRepository;
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

// @ExtendWith(MockitoExtension.class): Habilita el uso de Mockito en nuestros tests de JUnit 5.
// Es el "activador" que nos permite usar @Mock y @InjectMocks.
@ExtendWith(MockitoExtension.class)
class DetalleCompraServiceImplTest {

    // @Mock: Crea un objeto simulado (un "doble de prueba") de la dependencia.
    // En lugar de usar la implementación real que se conecta a la BD, usamos este mock
    // para controlar su comportamiento y simular sus respuestas.
    @Mock
    private DetalleCompraRepository repository;

    @Mock
    private DetalleCompraMapper mapper;

    // @InjectMocks: Crea una instancia real de la clase que queremos probar (DetalleCompraServiceImpl)
    // e inyecta automáticamente los mocks declarados con @Mock en ella.
    @InjectMocks
    private DetalleCompraServiceImpl service;

    // --- Variables de prueba ---
    // Son los objetos que usaremos en los diferentes escenarios de prueba.
    private DetalleCompra detalleCompra;
    private DetalleCompraRequestDto requestDto;
    private DetalleCompraResponseDto responseDto;

    // @BeforeEach: Este método se ejecuta ANTES de cada uno de los tests en esta clase.
    // Garantiza que cada test comience con un conjunto de datos "limpio" y predecible.
    @BeforeEach
    void setUp() {
        // Entidad de dominio
        detalleCompra = new DetalleCompra();
        detalleCompra.setIdDetalleCompra(1L);
        detalleCompra.setCantidad(10);
        // Simulamos las relaciones
        detalleCompra.setCompra(new Compra());
        detalleCompra.setProducto(new Producto());

        // DTO de entrada (para crear o actualizar)
        requestDto = new DetalleCompraRequestDto();
        requestDto.setIdCompra(1L);
        requestDto.setIdProducto(1L);
        requestDto.setCantidad(10);

        // DTO de salida (lo que el servicio devuelve)
        responseDto = new DetalleCompraResponseDto();
        responseDto.setIdDetalleCompra(1L);
        responseDto.setNombreProducto("Laptop");
        responseDto.setCantidad(10);
    }

    @Test
    void create_DebeCrearUnDetalleDeCompra() {
        // --- Given (Dado un escenario) ---
        // Aquí configuramos el comportamiento esperado de nuestros mocks.
        // 1. Cuando se llame al mapper para convertir de DTO a entidad, devuelve nuestro objeto 'detalleCompra'.
        when(mapper.toEntity(requestDto)).thenReturn(detalleCompra);
        // 2. Cuando el repositorio guarde CUALQUIER instancia de DetalleCompra, devuelve nuestro objeto 'detalleCompra'.
        when(repository.save(any(DetalleCompra.class))).thenReturn(detalleCompra);
        // 3. Cuando se llame al mapper para convertir la entidad guardada a DTO de respuesta, devuelve nuestro 'responseDto'.
        when(mapper.toResponse(detalleCompra)).thenReturn(responseDto);

        // --- When (Cuando ejecutamos la acción) ---
        // Llamamos al método del servicio que queremos probar.
        DetalleCompraResponseDto result = service.create(requestDto);

        // --- Then (Entonces verificamos el resultado) ---
        // Usamos AssertJ para hacer las verificaciones de forma fluida y legible.
        // 1. Aseguramos que el resultado no sea nulo.
        assertThat(result).isNotNull();
        // 2. Aseguramos que la cantidad en el resultado sea la que esperamos.
        assertThat(result.getCantidad()).isEqualTo(10);

        // verify(...): Confirma que un método de un mock fue invocado.
        // Aquí nos aseguramos de que el método save() del repositorio se llamó exactamente una vez.
        verify(repository, times(1)).save(any(DetalleCompra.class));
    }

    @Test
    void findAll_DebeRetornarListaDeDetalles() {
        // Given: Simula que el repositorio devuelve una lista con un detalle.
        when(repository.findAll()).thenReturn(List.of(detalleCompra));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        // When: Llamamos al método.
        List<DetalleCompraResponseDto> result = service.findAll();

        // Then: Verificamos que la lista no es nula y tiene un elemento.
        assertThat(result).isNotNull().hasSize(1);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_DebeRetornarDetalleCuandoExiste() {
        // Given: Simula que el repositorio encuentra el detalle por su ID.
        when(repository.findById(1L)).thenReturn(Optional.of(detalleCompra));
        when(mapper.toResponse(detalleCompra)).thenReturn(responseDto);

        // When: Llamamos al método.
        DetalleCompraResponseDto result = service.findById(1L);

        // Then: Verificamos que el resultado es el esperado.
        assertThat(result).isNotNull();
        assertThat(result.getIdDetalleCompra()).isEqualTo(1L);
    }

    @Test
    void findById_DebeLanzarExcepcionCuandoNoExiste() {
        // Given: Simula que el repositorio NO encuentra nada para un ID específico.
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then: Verificamos que al llamar al método se lanza la excepción esperada.
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no encontrada");
    }
}
