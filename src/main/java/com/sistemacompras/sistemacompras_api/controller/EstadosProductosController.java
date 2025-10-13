package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.EstadosProductosResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opciones/estados")
public class EstadosProductosController {

    @GetMapping("/producto")
    public ResponseEntity<List<EstadosProductosResponseDto>> getEstadosProducto() {
        List<EstadosProductosResponseDto> estados = Arrays.stream(EstadoProducto.values())
                .map(estado -> new EstadosProductosResponseDto(
                        estado.name(),
                        estado.getDescripcion()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(estados);
    }
}