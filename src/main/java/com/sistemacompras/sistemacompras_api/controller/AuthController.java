package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.config.JwtTokenProvider;
import com.sistemacompras.sistemacompras_api.dto.LoginRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MensajeResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para autenticación de usuarios.
 * Maneja el login y devuelve un JWT si las credenciales son correctas.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder; // NUEVO

    // Constructor actualizado
    public AuthController(UsuarioService usuarioService,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder; // NUEVO
    }

    @PostMapping("/login")
    public ResponseEntity<MensajeResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        // Buscamos el usuario por su email
        Usuario usuario = usuarioService.findEntityByEmail(loginRequest.getEmail());

        // MODIFICADO: Usamos passwordEncoder.matches() en lugar de equals()
        if (usuario != null && passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {

            // Generamos un token JWT para este usuario
            String token = jwtTokenProvider.generarToken(usuario.getEmail());

            MensajeResponseDto response = new MensajeResponseDto(
                    "Login correcto",
                    token
            );

            return ResponseEntity.ok(response);
        }

        // Si las credenciales son inválidas
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MensajeResponseDto("Credenciales inválidas", null));
    }
}

