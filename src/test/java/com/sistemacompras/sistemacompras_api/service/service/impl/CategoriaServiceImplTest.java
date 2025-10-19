package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.CategoriaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CategoriaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Categoria;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.CategoriaMapper;
import com.sistemacompras.sistemacompras_api.repository.CategoriaRepository;
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

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository repository;

    @Mock
    private CategoriaMapper mapper;

    @InjectMocks
    private CategoriaServiceImpl service;

    private Categoria categoria;
    private CategoriaRequestDto requestDto;
    private CategoriaResponseDto responseDto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");

        requestDto = new CategoriaRequestDto();
        requestDto.setNombre("Electrónica");

        responseDto = new CategoriaResponseDto();
        responseDto.setId(1L);
        responseDto.setNombre("Electrónica");
    }

    @Test
    void create_DebeCrearCategoria() {
        when(repository.existsByNombreIgnoreCase(requestDto.getNombre())).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(categoria);
        when(repository.save(any(Categoria.class))).thenReturn(categoria);
        when(mapper.toResponse(categoria)).thenReturn(responseDto);

        CategoriaResponseDto result = service.create(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Electrónica");
        verify(repository, times(1)).save(any(Categoria.class));
    }

    @Test
    void create_DebeLanzarExcepcionSiNombreYaExiste() {
        when(repository.existsByNombreIgnoreCase(requestDto.getNombre())).thenReturn(true);

        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya existe");

        verify(repository, never()).save(any(Categoria.class));
    }

    @Test
    void findAll_DebeRetornarListaDeCategorias() {
        when(repository.findAll()).thenReturn(List.of(categoria));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        List<CategoriaResponseDto> result = service.findAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_DebeRetornarCategoriaSiExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        when(mapper.toResponse(categoria)).thenReturn(responseDto);

        CategoriaResponseDto result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_DebeLanzarExcepcionSiNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no encontrada");
    }

    @Test
    void update_DebeActualizarCategoria() {
        Long categoriaId = 1L;
        CategoriaRequestDto updateDto = new CategoriaRequestDto();
        updateDto.setNombre("Hogar");

        when(repository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(repository.existsByNombreIgnoreCase(updateDto.getNombre())).thenReturn(false);
        when(repository.save(any(Categoria.class))).thenReturn(categoria);
        when(mapper.toResponse(categoria)).thenReturn(responseDto);

        service.update(categoriaId, updateDto);

        verify(mapper, times(1)).updateEntityFromRequest(updateDto, categoria);
        verify(repository, times(1)).save(categoria);
    }

    @Test
    void delete_DebeEliminarCategoria() {
        Long categoriaId = 1L;
        when(repository.existsById(categoriaId)).thenReturn(true);

        service.delete(categoriaId);

        verify(repository, times(1)).deleteById(categoriaId);
    }

    @Test
    void delete_DebeLanzarExcepcionSiNoExiste() {
        Long categoriaId = 99L;
        when(repository.existsById(categoriaId)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(categoriaId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no encontrada");

        verify(repository, never()).deleteById(anyLong());
    }
}

