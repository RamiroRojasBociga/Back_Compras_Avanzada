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

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ===========================================================
                // 💡 DESACTIVAMOS CSRF PARA PRUEBAS EN SWAGGER
                // ===========================================================
                .csrf(csrf -> csrf.disable())

                // ===========================================================
                // 💡 CONFIGURAMOS LAS RUTAS PÚBLICAS
                // ===========================================================
                .authorizeHttpRequests(auth -> auth
                        // ✅ ENDPOINTS QUE NO REQUIEREN TOKEN JWT
                        .requestMatchers(
                                "/api/auth/**",
                                "/doc/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",

                                // 💡💡💡 SE AGREGA RUTA COMPLETA DE PROVEEDORES 💡💡💡
                                "/api/proveedores/**"
                        ).permitAll()

                        // 🔒 TODO LO DEMÁS REQUIERE AUTENTICACIÓN
                        .anyRequest().authenticated()
                )

                // ===========================================================
                // 💡 DESACTIVAMOS SESIONES Y CONFIGURAMOS FILTRO JWT
                // ===========================================================
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // ⚠️ IMPORTANTE: el filtro JWT DEBE IR DESPUÉS DE LAS RUTAS PÚBLICAS
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ===========================================================
    // 💡 ENCODER DE CONTRASEÑAS
    // ===========================================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        DelegatingPasswordEncoder delegatingEncoder =
                new DelegatingPasswordEncoder(encodingId, encoders);
        delegatingEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        return delegatingEncoder;
    }

    // ===========================================================
    // 💡 AUTHENTICATION MANAGER PARA LOGIN
    // ===========================================================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
