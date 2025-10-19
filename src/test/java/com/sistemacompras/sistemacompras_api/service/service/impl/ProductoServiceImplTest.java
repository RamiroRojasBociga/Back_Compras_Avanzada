package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Producto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.ProductoMapper;
import com.sistemacompras.sistemacompras_api.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class): Habilita el uso de Mockito en esta clase de test.
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    // @Mock: Crea un objeto simulado (mock) para las dependencias del servicio.
    @Mock
    private ProductoRepository repository;

    @Mock
    private ProductoMapper mapper;

    // @InjectMocks: Crea una instancia real de ProductoServiceImpl e inyecta los mocks.
    @InjectMocks
    private ProductoServiceImpl service;

    // --- Objetos de prueba ---
    private Producto producto;
    private ProductoRequestDto requestDto;
    private ProductoResponseDto responseDto;

    // @BeforeEach: Se ejecuta antes de cada test para preparar un estado limpio.
    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setIdProducto(1L);
        producto.setNombre("Laptop Gamer");
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setStock(10);
        producto.setEstado(EstadoProducto.ACTIVO);

        requestDto = new ProductoRequestDto();
        requestDto.setNombre("Laptop Gamer");
        requestDto.setEstado(EstadoProducto.ACTIVO);

        responseDto = new ProductoResponseDto();
        responseDto.setIdProducto(1L);
        responseDto.setNombre("Laptop Gamer");
        responseDto.setEstado(EstadoProducto.ACTIVO);
    }

    // --- Tests para el método create() ---
    @Test
    void create_DebeCrearProductoExitosamente() {
        // Given
        when(repository.existsByNombreIgnoreCase(requestDto.getNombre())).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(producto);
        when(repository.save(any(Producto.class))).thenReturn(producto);
        when(mapper.toResponse(producto)).thenReturn(responseDto);

        // When
        ProductoResponseDto result = service.create(requestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Laptop Gamer");
        verify(repository).save(any(Producto.class));
    }

    // --- Tests para los nuevos métodos de estado ---
    @Test
    void findByEstado_DebeRetornarListaDeProductosFiltradaPorEstado() {
        // Given: Simula que el repositorio encuentra productos con estado ACTIVO.
        when(repository.findByEstado(EstadoProducto.ACTIVO)).thenReturn(List.of(producto));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        // When: Buscamos productos activos.
        List<ProductoResponseDto> result = service.findByEstado(EstadoProducto.ACTIVO);

        // Then: Verificamos que el resultado es el esperado.
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getEstado()).isEqualTo(EstadoProducto.ACTIVO);
        verify(repository).findByEstado(EstadoProducto.ACTIVO);
    }

    @Test
    void cambiarEstado_DebeActualizarElEstadoDelProducto() {
        // Given: Simula que el producto existe.
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        // When: Llamamos al método para cambiar el estado a INACTIVO.
        service.cambiarEstado(1L, EstadoProducto.INACTIVO);

        // Then: Verificamos que el estado del objeto 'producto' fue modificado
        // y que el método save() fue llamado para persistir el cambio.
        assertThat(producto.getEstado()).isEqualTo(EstadoProducto.INACTIVO);
        verify(repository).save(producto);
    }

    @Test
    void cambiarEstado_DebeLanzarExcepcionSiProductoNoExiste() {
        // Given: Simula que el producto NO existe.
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then: Verificamos que se lanza la excepción correcta.
        assertThatThrownBy(() -> service.cambiarEstado(99L, EstadoProducto.INACTIVO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producto 99 no encontrada");

        verify(repository, never()).save(any(Producto.class));
    }

    @Test
    void findProductosActivos_DebeLlamarAFindByEstadoConActivo() {
        // Este test verifica que el método de conveniencia llama al método principal
        // con el parámetro correcto.

        // Given
        when(repository.findByEstado(EstadoProducto.ACTIVO)).thenReturn(List.of(producto));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        // When
        service.findProductosActivos();

        // Then: Verificamos que el método subyacente fue llamado con el estado correcto.
        verify(repository).findByEstado(EstadoProducto.ACTIVO);
    }

    // --- Otros tests CRUD (findById, findAll, update, delete) ---
    // (Estos serían similares a los de los otros servicios)
    @Test
    void findById_DebeRetornarProductoCuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        when(mapper.toResponse(producto)).thenReturn(responseDto);

        ProductoResponseDto result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getIdProducto()).isEqualTo(1L);
    }
}
