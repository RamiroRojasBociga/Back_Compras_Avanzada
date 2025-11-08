package com.sistemacompras.sistemacompras_api.dto;

/**
 * DTO para la peticion de login.
 * Contiene las credenciales del usuario (email y password).
 */
public class LoginRequestDto {

    // Email del usuario (usado como identificador unico)
    private String email;

    // Contraseña del usuario
    private String password;

    // Constructor vacio requerido por Jackson
    public LoginRequestDto() {
    }

    // Constructor con parametros
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
