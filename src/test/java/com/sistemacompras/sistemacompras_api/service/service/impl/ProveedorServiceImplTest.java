package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import com.sistemacompras.sistemacompras_api.enums.EstadoProveedor;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.ProveedorMapper;
import com.sistemacompras.sistemacompras_api.repository.ProveedorRepository;
import com.sistemacompras.sistemacompras_api.enums.EstadoProveedor;


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

// @ExtendWith(MockitoExtension.class): Habilita el uso de Mockito en JUnit 5,
// permitiendo crear "dobles de prueba" para aislar la clase que estamos testeando.
@ExtendWith(MockitoExtension.class)
class ProveedorServiceImplTest {

    // @Mock: Crea un objeto simulado para una dependencia. En este caso, simulamos
    // el repositorio para no depender de una base de datos real durante los tests unitarios.
    @Mock
    private ProveedorRepository repository;

    @Mock
    private ProveedorMapper mapper;

    // @InjectMocks: Crea una instancia real de la clase a probar (ProveedorServiceImpl)
    // e inyecta automáticamente los mocks (@Mock) en ella.
    @InjectMocks
    private ProveedorServiceImpl service;

    // --- Objetos de prueba ---
    private Proveedor proveedor;
    private ProveedorRequestDto requestDto;
    private ProveedorResponseDto responseDto;

    // @BeforeEach: Este método se ejecuta antes de cada test.
    // Prepara un estado inicial limpio para que cada prueba sea independiente.
    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setIdProveedor(1L);
        proveedor.setNombre("TechGlobal S.A.");
        proveedor.setEstado(EstadoProveedor.ACTIVO);

        requestDto = new ProveedorRequestDto();
        requestDto.setNombre("TechGlobal S.A.");

        responseDto = new ProveedorResponseDto();
        responseDto.setIdProveedor(1L);
        responseDto.setNombre("TechGlobal S.A.");
        responseDto.setEstado("ACTIVO");
    }

    // --- Tests para el método create() ---
    @Test
    void create_DebeCrearProveedorExitosamente() {
        // Given (Dado): Configuramos los mocks para el escenario de éxito.
        when(repository.existsByNombreIgnoreCase("TechGlobal S.A.")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(proveedor);
        when(repository.save(any(Proveedor.class))).thenReturn(proveedor);
        when(mapper.toResponse(proveedor)).thenReturn(responseDto);

        // When (Cuando): Ejecutamos la acción a probar.
        ProveedorResponseDto result = service.create(requestDto);

        // Then (Entonces): Verificamos los resultados.
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("TechGlobal S.A.");
        verify(repository).save(any(Proveedor.class));
    }

    // --- Test para el caso de error en create() ---
    @Test
    void create_DebeLanzarExcepcionSiNombreYaExiste() {
        // Given: Simulamos que el proveedor ya existe.
        when(repository.existsByNombreIgnoreCase("TechGlobal S.A.")).thenReturn(true);

        // When & Then: Verificamos que se lanza la excepción correcta.
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre de categoría ya existe"); // Corresponde al mensaje de tu implementación

        // Verificamos que el método save() NUNCA se llamó.
        verify(repository, never()).save(any(Proveedor.class));
    }

    // --- Tests para los métodos de lectura (find) ---
    @Test
    void findAll_DebeRetornarTodosLosProveedores() {
        when(repository.findAll()).thenReturn(List.of(proveedor));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        List<ProveedorResponseDto> result = service.findAll();

        assertThat(result).isNotNull().hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void findById_DebeRetornarProveedorSiExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(mapper.toResponse(proveedor)).thenReturn(responseDto);

        ProveedorResponseDto result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getIdProveedor()).isEqualTo(1L);
    }

    // --- Tests para update() y delete() ---
    @Test
    void update_DebeActualizarProveedor() {
        Long proveedorId = 1L;
        ProveedorRequestDto updateDto = new ProveedorRequestDto();
        updateDto.setNombre("Nuevo Nombre");

        when(repository.findById(proveedorId)).thenReturn(Optional.of(proveedor));
        when(repository.existsByNombreIgnoreCase("Nuevo Nombre")).thenReturn(false);
        when(repository.save(any(Proveedor.class))).thenReturn(proveedor);

        service.update(proveedorId, updateDto);

        verify(mapper).updateEntityFromRequest(updateDto, proveedor);
        verify(repository).save(proveedor);
    }

    @Test
    void delete_DebeEliminarProveedor() {
        Long proveedorId = 1L;
        when(repository.existsById(proveedorId)).thenReturn(true);

        service.delete(proveedorId);

        verify(repository).deleteById(proveedorId);
    }
}
