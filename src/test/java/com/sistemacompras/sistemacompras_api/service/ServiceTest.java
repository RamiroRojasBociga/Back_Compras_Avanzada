package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.CategoriaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CategoriaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Categoria;
import com.sistemacompras.sistemacompras_api.mapper.CategoriaMapper;
import com.sistemacompras.sistemacompras_api.repository.CategoriaRepository;
import com.sistemacompras.sistemacompras_api.service.service.impl.CategoriaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith: Habilita Mockito para las pruebas
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock  // Simula el repository (base de datos falsa)
    private CategoriaRepository categoriaRepository;

    @Mock  // Simula el mapper
    private CategoriaMapper categoriaMapper;

    @InjectMocks  // Inyecta los mocks en el servicio real
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;
    private CategoriaRequestDto requestDto;
    private CategoriaResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Configura datos de prueba antes de cada test
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónicos");

        requestDto = new CategoriaRequestDto();
        requestDto.setNombre("Electrónicos");

        responseDto = new CategoriaResponseDto();
        responseDto.setId(1L);
        responseDto.setNombre("Electrónicos");
    }

    @Test
    void testFindAll() {
        // Given (Dado): Configura el escenario
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);
        when(categoriaMapper.toResponseList(categorias)).thenReturn(Arrays.asList(responseDto));

        // When (Cuando): Ejecuta el método a probar
        List<CategoriaResponseDto> result = categoriaService.findAll();

        // Then (Entonces): Verifica los resultados
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electrónicos", result.get(0).getNombre());

        // Verifica que se llamó al repository
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Given
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toResponse(categoria)).thenReturn(responseDto);

        // When
        CategoriaResponseDto result = categoriaService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electrónicos", result.getNombre());
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then: Verifica que lance excepción
        assertThrows(RuntimeException.class, () -> {
            categoriaService.findById(999L);
        });
    }

    @Test
    void testCreate() {
        // Given
        when(categoriaMapper.toEntity(requestDto)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toResponse(categoria)).thenReturn(responseDto);

        // When
        CategoriaResponseDto result = categoriaService.create(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoriaRepository, times(1)).save(categoria);
    }
}
