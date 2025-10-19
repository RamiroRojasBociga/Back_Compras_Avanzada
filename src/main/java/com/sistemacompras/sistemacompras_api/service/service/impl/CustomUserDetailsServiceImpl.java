package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import java.util.Collections;

// @Service: Marca esta clase como un componente de Spring para que pueda ser inyectado en otras partes de la aplicación.
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    // Dependencia del repositorio para acceder a la base de datos.
    private final UsuarioRepository usuarioRepository;

    // Constructor para la inyección de dependencias. Spring se encarga de pasar una instancia de UsuarioRepository.
    public CustomUserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Este es el ÚNICO método de la interfaz UserDetailsService.
     * Spring Security lo llama automáticamente durante el proceso de autenticación (login).
     * Su única responsabilidad es: "dado un nombre de usuario (en tu caso, un email),
     * busca al usuario en la base de datos y devuélvelo en un formato que Spring Security entienda (UserDetails)".
     *
     * @param email El "nombre de usuario" que el usuario ingresó en el formulario de login.
     * @return Un objeto UserDetails que contiene los datos del usuario (username, password, roles).
     * @throws UsernameNotFoundException Si el usuario no se encuentra en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Imprime el email que se está intentando autenticar. Útil para depuración.
        System.out.println(email);

        // 2. Busca el usuario en la base de datos usando el email proporcionado.
        Usuario usuario = usuarioRepository.findByEmail(email)
                // Si el usuario no se encuentra, lanza una excepción que Spring Security sabe cómo manejar.
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // 3. Convierte tu objeto `Usuario` de la base de datos a un objeto `User` que Spring Security entiende.
        // El objeto `User` implementa la interfaz `UserDetails`.
        // Se le pasa:
        //   - El email como nombre de usuario.
        //   - La contraseña (que ya está hasheada en la BD) tal cual.
        //   - Una lista de roles/autoridades. En tu caso, está vacía (Collections.emptyList()),
        //     lo que significa que por ahora no estás manejando roles (ej. "ROLE_ADMIN", "ROLE_USER").
        return new User(usuario.getEmail(), usuario.getPassword(), Collections.emptyList());
    }
}
