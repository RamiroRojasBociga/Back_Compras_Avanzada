package com.sistemacompras.sistemacompras_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

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
        // Definimos qué encoder usar para NUEVAS contraseñas
        String encodingId = "bcrypt";

        // Creamos un mapa con todos los encoders que queremos soportar
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());  // Para contraseñas nuevas
        encoders.put("noop", NoOpPasswordEncoder.getInstance()); // Para contraseñas existentes

        // Creamos el DelegatingPasswordEncoder
        DelegatingPasswordEncoder delegatingEncoder =
                new DelegatingPasswordEncoder(encodingId, encoders);

        // IMPORTANTE: Esto permite validar contraseñas SIN prefijo como texto plano
        // Es necesario para que funcione con las contraseñas que ya tienen {noop} en la BD
        delegatingEncoder.setDefaultPasswordEncoderForMatches(
                NoOpPasswordEncoder.getInstance()
        );

        return delegatingEncoder;
    }


     //Gestiona la autenticación para el login (útil si luego lo usas con AuthenticationManager)

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
