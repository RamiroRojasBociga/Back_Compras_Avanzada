package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.config.JwtTokenProvider;
import com.sistemacompras.sistemacompras_api.dto.LoginRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MensajeResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Inyectamos el servicio de usuario y el generador de tokens
    public AuthController(UsuarioService usuarioService, JwtTokenProvider jwtTokenProvider) {
        this.usuarioService = usuarioService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


     //Recibe email y password → valida → devuelve un JWT

    @PostMapping("/login")
    public ResponseEntity<MensajeResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        // 1️⃣ Buscamos el usuario por su email
        Usuario usuario = usuarioService.findEntityByEmail(loginRequest.getEmail());

        // Verificamos que exista y que la contraseña sea correcta
        if (usuario != null && usuario.getPassword().equals(loginRequest.getPassword())) {

            // Generamos un token JWT para este usuario
            String token = jwtTokenProvider.generarToken(usuario.getEmail());

            // Construimos una respuesta con mensaje + token
            MensajeResponseDto response = new MensajeResponseDto(
                    "Login correcto",
                    token
            );

            // Devolvemos el token al cliente (Postman o Swagger)
            return ResponseEntity.ok(response);
        }

        // Si las credenciales son inválidas
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MensajeResponseDto("Credenciales inválidas", null));
    }
}
