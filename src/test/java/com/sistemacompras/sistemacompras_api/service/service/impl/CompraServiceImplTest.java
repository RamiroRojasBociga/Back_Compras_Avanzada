package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Compra;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.CompraMapper;
import com.sistemacompras.sistemacompras_api.repository.CompraRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceImplTest {

    @Mock
    private CompraRepository repository;

    @Mock
    private CompraMapper mapper;

    @InjectMocks
    private CompraServiceImpl service;

    private Compra compra;
    private CompraRequestDto requestDto;
    private CompraResponseDto responseDto;

    @BeforeEach
    void setUp() {
        compra = new Compra();
        compra.setIdCompra(1L);
        compra.setFecha(LocalDate.now());
        compra.setEstado("PENDIENTE");

        requestDto = new CompraRequestDto();
        requestDto.setIdUsuario(1L);
        requestDto.setIdProveedor(1L);
        requestDto.setFecha(LocalDate.now());
        requestDto.setEstado("PENDIENTE");

        responseDto = new CompraResponseDto();
        responseDto.setIdCompra(1L);
        responseDto.setNombreUsuario("Usuario de Prueba");
        responseDto.setNombreProveedor("Proveedor de Prueba");
        responseDto.setFecha(LocalDate.now());
        responseDto.setEstado("PENDIENTE");
    }

    @Test
    void create_DebeCrearCompra() {
        when(mapper.toEntity(requestDto)).thenReturn(compra);
        when(repository.save(any(Compra.class))).thenReturn(compra);
        when(mapper.toResponse(compra)).thenReturn(responseDto);

        CompraResponseDto result = service.create(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo("PENDIENTE");
        verify(repository, times(1)).save(any(Compra.class));
    }

    @Test
    void findAll_DebeRetornarListaDeCompras() {
        when(repository.findAll()).thenReturn(List.of(compra));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        List<CompraResponseDto> result = service.findAll();

        assertThat(result).isNotNull().hasSize(1);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_DebeRetornarCompraSiExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(compra));
        when(mapper.toResponse(compra)).thenReturn(responseDto);

        CompraResponseDto result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getIdCompra()).isEqualTo(1L);
    }

    @Test
    void findById_DebeLanzarExcepcionSiNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Compra 99 no encontrada");
    }

    @Test
    void update_DebeActualizarCompra() {
        Long compraId = 1L;
        CompraRequestDto updateDto = new CompraRequestDto();
        updateDto.setEstado("COMPLETADO");

        when(repository.findById(compraId)).thenReturn(Optional.of(compra));
        when(repository.save(any(Compra.class))).thenReturn(compra);
        when(mapper.toResponse(compra)).thenReturn(responseDto);

        service.update(compraId, updateDto);

        verify(mapper, times(1)).updateEntityFromRequest(updateDto, compra);
        verify(repository, times(1)).save(compra);
    }

    @Test
    void delete_DebeEliminarCompraSiExiste() {
        Long compraId = 1L;
        when(repository.existsById(compraId)).thenReturn(true);

        service.delete(compraId);

        verify(repository, times(1)).deleteById(compraId);
    }

    @Test
    void delete_DebeLanzarExcepcionSiNoExiste() {
        Long compraId = 99L;
        when(repository.existsById(compraId)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(compraId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Compra 99 no encontrada");

        verify(repository, never()).deleteById(anyLong());
    }
}
