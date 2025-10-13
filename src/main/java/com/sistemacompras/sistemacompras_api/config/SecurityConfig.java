package com.sistemacompras.sistemacompras_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor: inyectamos el filtro JWT
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


     //Define la cadena de filtros de seguridad de Spring Security

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF (útil para pruebas con Postman o Swagger)
                .csrf(csrf -> csrf.disable())

                // Configuramos qué rutas son públicas o protegidas
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (sin necesidad de token)
                        .requestMatchers("/api/auth/**", "/doc/**", "/v3/api-docs/**").permitAll()

                        // Todo lo demás requiere autenticación JWT
                        .anyRequest().authenticated()
                )

                // Agregamos el filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Indicamos que no queremos sesiones en el servidor (JWT = stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


     // Codificador de contraseñas (sin cifrado, solo para pruebas locales)

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


     //Gestiona la autenticación para el login (útil si luego lo usas con AuthenticationManager)

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
