package com.sistemacompras.sistemacompras_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// Configuracion de CORS para permitir peticiones desde Angular
@Configuration
public class CorsConfig {

    // Lee el origen permitido desde application.properties
    @Value("${cors.allowed.origins:http://localhost:4200}")
    private String allowedOrigins;

    // Bean que configura CORS para toda la aplicacion
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origenes permitidos (separados por coma si son varios)
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // Metodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos (todos)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permite envio de cookies/credenciales
        configuration.setAllowCredentials(true);

        // Tiempo de cache para preflight (1 hora)
        configuration.setMaxAge(3600L);

        // Aplica la configuracion a todas las rutas /api/**
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
