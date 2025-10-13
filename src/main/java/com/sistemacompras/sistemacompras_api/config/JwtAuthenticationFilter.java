package com.sistemacompras.sistemacompras_api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Clase que creamos antes, encargada de generar y validar el token JWT
    private final JwtTokenProvider jwtTokenProvider;

    // Servicio que carga los detalles del usuario (por correo, en este caso)
    private final UserDetailsService userDetailsService;

    // Constructor con inyección de dependencias
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }


     // Método principal del filtro que intercepta todas las solicitudes HTTP

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Leemos el encabezado "Authorization" donde viene el token
        String header = request.getHeader("Authorization");

        // Verificamos que no sea nulo y que empiece con "Bearer "
        if (header != null && header.startsWith("Bearer ")) {

            // Quitamos el prefijo "Bearer " y obtenemos solo el token
            String token = header.substring(7);

            // Obtenemos el email del usuario a partir del token
            String email = jwtTokenProvider.obtenerEmailDeToken(token);

            // Verificamos que el email exista y que aún no haya autenticación activa
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Buscamos el usuario en la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Validamos que el token sea válido (firma y expiración)
                if (jwtTokenProvider.validarToken(token)) {

                    // Creamos un objeto de autenticación con los datos del usuario
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, // usuario autenticado
                                    null,        // sin credenciales (ya fue validado)
                                    userDetails.getAuthorities() // roles o permisos (vacío por ahora)
                            );

                    // Asociamos los detalles de la solicitud (IP, sesión, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Guardamos la autenticación en el contexto de seguridad de Spring
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Dejamos que el flujo de la petición continúe
        filterChain.doFilter(request, response);
    }
}
