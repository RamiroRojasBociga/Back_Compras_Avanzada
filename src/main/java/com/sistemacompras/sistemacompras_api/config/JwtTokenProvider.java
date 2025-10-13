package com.sistemacompras.sistemacompras_api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Clave secreta (leída del application.properties)
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Tiempo de expiración (en milisegundos)
    @Value("${jwt.expirationMillis}")
    private long jwtExpirationMillis;


     // Genera un token JWT con el email del usuario

    public String generarToken(String email) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + jwtExpirationMillis);

        return Jwts.builder()
                .setSubject(email) // guardamos el email en el campo "sub" (subject)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(getFirmaKey(), SignatureAlgorithm.HS256)
                .compact();
    }


     // Devuelve el email guardado en el token (el "subject")

    public String obtenerEmailDeToken(String token) {
        Claims claims = obtenerClaims(token); // obtenemos los datos (claims)
        return claims.getSubject(); // el "subject" es el email
    }


     // Verifica que el token sea válido y no haya expirado

    public boolean validarToken(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


     // Extrae los datos (claims) del token usando la clave secreta

    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getFirmaKey()) // clave para verificar la firma
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


     //Convierte la clave secreta en un objeto Key
    private Key getFirmaKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
